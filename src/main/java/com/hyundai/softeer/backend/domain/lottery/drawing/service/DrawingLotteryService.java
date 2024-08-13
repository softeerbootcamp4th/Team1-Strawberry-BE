package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
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

    public DrawingInfoDtos getDrawingGameInfo(SubEventRequest subEventRequest) {
        List<DrawingLotteryEvent> drawingEvents = drawingLotteryRepository.findBySubEventId(subEventRequest.getSubEventId());

        if (drawingEvents.isEmpty()) {
            throw new DrawingNotFoundException();
        }

        return new DrawingInfoDtos(drawingEvents.stream()
                .map(DrawingGameInfoDto::fromEntity)
                .toList());
    }

    public DrawingScoreDto getDrawingScore(User authenticatedUser, DrawingScoreRequest drawingScoreRequest) {
        DrawingLotteryEvent drawingEvent = drawingLotteryRepository.findBySubEventIdAndSequence(drawingScoreRequest.getSubEventId(), drawingScoreRequest.getSequence())
                .orElseThrow(DrawingNotFoundException::new);

        List<PositionDto> answerPoints = ParseUtil.parsePositionsFromJson(drawingEvent.getDrawPointsJsonUrl());
        double accuracy = calculateAverageEuclideanDistance(drawingScoreRequest.getPositions(), answerPoints);

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
                .build();
    }

    private double calculateAverageEuclideanDistance(List<PositionDto> userPoints, List<PositionDto> answerPoints) {
        double totalScore = 0.0;
        int count = 0;
        double maxDistance = 100 * Math.sqrt(2); // 최대 거리 계산

        for (PositionDto answerPoint : answerPoints) {
            double minDistance = Double.MAX_VALUE;

            // 각 answerPoint에 대해 userPoints의 최소 거리 계산
            for (PositionDto userPoint : userPoints) {
                double distance = Math.sqrt(Math.pow(answerPoint.getX() - userPoint.getX(), 2) + Math.pow(answerPoint.getY() - userPoint.getY(), 2));
                minDistance = Math.min(minDistance, distance);
            }

            // 점수 계산
            double score;
            if (minDistance >= maxDistance) {
                score = 0; // 최대 거리 이상일 경우 0점
            } else if (minDistance == 0) {
                score = 100; // 완벽히 일치할 경우 100점
            } else {
                score = 100 * (1 - (minDistance / maxDistance)); // 점수 계산
            }

            totalScore += score; // 전체 점수에 추가
            count++;
        }

        return count == 0 ? 0 : totalScore / count; // count가 0일 경우 0 반환
    }


    private double calculateAverageCosineSimilarity(List<PositionDto> userPoints, List<PositionDto> answerPoints) {
        double totalSimilarity = 0;
        int count = 0;

        for (PositionDto points : answerPoints) {
            double maxSimilarity = -1;
            for (PositionDto userPoint : userPoints) {
                double similarity = (points.getX() * userPoint.getX() + points.getY() * userPoint.getY()) /
                        (Math.sqrt(Math.pow(points.getX(), 2) + Math.pow(points.getY(), 2)) * Math.sqrt(Math.pow(userPoint.getX(), 2) + Math.pow(userPoint.getY(), 2)));
                maxSimilarity = Math.max(maxSimilarity, similarity);
            }
            double scaledScore = ((maxSimilarity + 1) / 2) * 100;
            totalSimilarity += scaledScore;
            count++;
        }

        return totalSimilarity / count;
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
            eventUser.updateScores(GAME_SCORE, totalScore);
            maxScore = totalScore;
            eventUserRepository.save(eventUser);
        }

        return DrawingTotalScoreDto.builder()
                .totalScore(totalScore)
                .maxScore(maxScore)
                .chance(eventUser.getChance())
                .build();
    }
}
