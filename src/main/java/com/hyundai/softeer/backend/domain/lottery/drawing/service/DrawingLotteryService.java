package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.exception.NoChanceUserException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.*;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.service.rank.DrawingRank;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.utils.ParseUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingLotteryService implements LotteryService {

    private final SubEventRepository subEventRepository;
    private final EventUserRepository eventUserRepository;
    private final DrawingLotteryRepository drawingLotteryRepository;
    private final ScoreCalculator scoreCalculator;
    private final DrawingRank drawingRank;

    public static final int RANK_COUNT = 20;

    @Value("${properties.event-id}")
    private Long eventId;

    @PostConstruct
    public void init() {
        subEventRepository.findByEventId(eventId)
                .stream()
                .filter(subEvent -> subEvent.getEventType().equals(SubEventType.DRAWING))
                .findFirst().ifPresent(subEvent -> drawingRank.updateRankingData(subEvent.getId(), RANK_COUNT));
    }

    @Transactional(readOnly = true)
    public DrawingLotteryLandDto getDrawingLotteryLand(long eventId) {
        List<SubEvent> events = subEventRepository.findByEventId(eventId);
        SubEvent drawing = findDrawingEvent(events);

        if (drawing == null) {
            throw new DrawingNotFoundException();
        }

        return DrawingLotteryLandDto.fromEntity(drawing);
    }

    public List<RankDto> getRankList(SubEventRequest subEventRequest) {
        return drawingRank.getRankList(subEventRequest, RANK_COUNT);
    }

    private SubEvent findDrawingEvent(List<SubEvent> subEvents) {
        for (SubEvent subEvent : subEvents) {
            if (subEvent.getEventType().equals(SubEventType.DRAWING)) {
                return subEvent;
            }
        }
        return null;
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

    @Transactional
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

    @Transactional
    public DrawingTotalScoreDto getDrawingTotalScore(User authenticatedUser, SubEventRequest subEventRequest) {
        return drawingRank.getDrawingTotalScore(authenticatedUser, subEventRequest, RANK_COUNT);
    }
}
