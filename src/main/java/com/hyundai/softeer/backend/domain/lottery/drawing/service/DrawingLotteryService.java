package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.NoChanceUserException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.*;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.utils.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingLotteryService implements LotteryService {
    public static final String FIRST_GAME_SCORE = "1_game_score";
    public static final String SECOND_GAME_SCORE = "2_game_score";
    public static final String THIRD_GAME_SCORE = "3_game_score";
    public static final String GAME_SCORE = "game_score";
    public static final List<Double> SCORE_WEIGHTS = List.of(0.25, 0.35, 0.4);

    private final SubEventRepository subEventRepository;
    private final EventUserRepository eventUserRepository;
    private final DrawingLotteryRepository drawingLotteryRepository;
    private final ScoreCalculator scoreCalculator;

    @Transactional(readOnly = true)
    public DrawingLotteryLandDto getDrawingLotteryLand(long eventId) {
        List<SubEvent> events = subEventRepository.findByEventId(eventId);
        SubEvent drawing = findDrawingEvent(events);

        if (drawing == null) {
            throw new DrawingNotFoundException();
        }

        return DrawingLotteryLandDto.fromEntity(drawing);
    }

    private SubEvent findDrawingEvent(List<SubEvent> subEvents) {
        for (SubEvent subEvent : subEvents) {
            if (subEvent.getEventType().equals(SubEventType.DRAWING)) {
                return subEvent;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RankDto> getRankList(SubEventRequest subEventRequest, int rankCount) {
        Pageable pageable = PageRequest.of(0, rankCount);
        List<RankDto> topNBySubEventId = eventUserRepository.findTopNBySubEventId(subEventRequest.getSubEventId(), pageable);

        for (int i = 0; i < topNBySubEventId.size(); i++) {
            topNBySubEventId.get(i).setRank(i + 1);
        }

        log.info("topNBySubEventId: {}", topNBySubEventId);

        return topNBySubEventId;
    }

    @Transactional
    public DrawingInfoDtos getDrawingGameInfo(User authenticatedUser, DrawingInfoRequest drawingInfoRequest) {
        List<DrawingLotteryEvent> drawingEvents = drawingLotteryRepository.findBySubEventId(drawingInfoRequest.getSubEventId());

        if (drawingEvents.isEmpty()) {
            throw new DrawingNotFoundException();
        }

        LocalDateTime now = LocalDateTime.now();

        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(authenticatedUser.getId(), drawingInfoRequest.getSubEventId())
                .orElseGet(() -> EventUser.builder()
                        .user(authenticatedUser)
                        .subEvent(subEventRepository.getReferenceById(drawingInfoRequest.getSubEventId()))
                        .lastVisitedAt(now)
                        .lastChargeAt(now)
                        .build());

        // TODO Refactor
        try {
            updateChanceAtNormalPlay(eventUser);
        } catch (NoChanceUserException e) {
            try {
                updateChanceAtSharedPlay(eventUser);
            } catch (NoChanceUserException ex) {
                try {
                    updateChanceAtExpectationPlay(eventUser);
                } catch (NoChanceUserException exc) {
                    throw new NoChanceUserException();
                }
            }
        }

        return DrawingInfoDtos.builder()
                .gameInfos(drawingEvents.stream()
                        .map(DrawingGameInfoDto::fromEntity)
                        .toList())
                .chance(eventUser.getChance())
                .build();
    }

    private void updateChanceAtSharedPlay(EventUser eventUser) {
        if (eventUser.getShareBonusChance() <= 0) {
            throw new NoChanceUserException();
        }

        eventUser.useShareBonusChance();
        eventUserRepository.save(eventUser);
    }

    private void updateChanceAtExpectationPlay(EventUser eventUser) {
        if (eventUser.getExpectationBonusChance() <= 0) {
            throw new NoChanceUserException();
        }

        eventUser.useExpectationBonusChance();
        eventUserRepository.save(eventUser);
    }


    private void updateChanceAtNormalPlay(EventUser eventUser) {
        eventUser.updateLastVisitedAtAndLastChargeAt();

        if (eventUser.getChance() == 0) {
            throw new NoChanceUserException();
        }

        eventUser.useChance();
        eventUserRepository.save(eventUser);
    }

    public DrawingScoreDto getDrawingScore(User authenticatedUser, DrawingScoreRequest drawingScoreRequest) {
        DrawingLotteryEvent drawingEvent = drawingLotteryRepository.findBySubEventIdAndSequence(drawingScoreRequest.getSubEventId(), drawingScoreRequest.getSequence())
                .orElseThrow(DrawingNotFoundException::new);

        List<PositionDto> answerPoints = ParseUtil.parsePositionsFromJson(drawingEvent.getDrawPointsJsonUrl());
        double accuracy = scoreCalculator.calculateAverageEuclideanDistance(drawingScoreRequest.getPositions(), answerPoints);

        Optional<EventUser> optionalEventUser = eventUserRepository.findByUserIdAndSubEventId(authenticatedUser.getId(), drawingScoreRequest.getSubEventId());

        optionalEventUser
                .ifPresent(eventUser -> {
                    eventUser.updateScores(drawingEvent.getSequence().toString() + "_game_score", accuracy);
                    eventUserRepository.save(eventUser);
                });
        optionalEventUser.orElseThrow(() -> new EventUserNotFoundException());


        return DrawingScoreDto.builder()
                .score(accuracy)
                .blurImgUrl(drawingEvent.getBlurImgUrl())
                // TODO 점수에 따른 랜덤 메세지 삽입
                .resultMsg("좀 만 더하면 고득점!")
                .resultDetail(drawingEvent.getResultDetail())
                .build();
    }

    public DrawingTotalScoreDto getDrawingTotalScore(User authenticatedUser, SubEventRequest subEventRequest) {
        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(authenticatedUser.getId(), subEventRequest.getSubEventId())
                .orElseThrow(() -> new EventUserNotFoundException());

        Map<String, Object> scores = eventUser.getScores();

        double firstScore = (double) scores.getOrDefault(FIRST_GAME_SCORE, 0.0);
        double secondScore = (double) scores.getOrDefault(SECOND_GAME_SCORE, 0.0);
        double thirdScore = (double) scores.getOrDefault(THIRD_GAME_SCORE, 0.0);

        double totalScore = firstScore * SCORE_WEIGHTS.get(0) + secondScore * SCORE_WEIGHTS.get(1) + thirdScore * SCORE_WEIGHTS.get(2);
        double maxScore = (double) scores.getOrDefault(GAME_SCORE, 0.0);

        if (totalScore > maxScore) {
            maxScore = totalScore;
            eventUser.updateScores(GAME_SCORE, totalScore);
            eventUser.updateGameScore(totalScore);
            eventUserRepository.save(eventUser);
        }

        return DrawingTotalScoreDto.builder()
                .totalScore(totalScore)
                .maxScore(maxScore)
                .chance(eventUser.getChance())
                .expectationBonusChance(eventUser.getExpectationBonusChance())
                .shareBonusChance(eventUser.getShareBonusChance())
                .build();
    }
}
