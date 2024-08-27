package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.config.S3Config;
import com.hyundai.softeer.backend.global.utils.ParseUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingLotteryService implements LotteryService {
    @Autowired
    private final MessageSource messageSource;

    private final SubEventRepository subEventRepository;
    private final EventUserRepository eventUserRepository;
    private final DrawingLotteryRepository drawingLotteryRepository;
    private final ScoreCalculator scoreCalculator;
    private final DrawingRank drawingRank;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final Random random = new Random();

    public static final int RANK_COUNT = 20;
    public static final String BUCKET_DIR = "/preview/";

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

        String resultMsg = getRandomMessageForScore((int) accuracy);

        return DrawingScoreDto.builder()
                .score(accuracy)
                .blurImgUrl(drawingEvent.getBlurImgUrl())
                .resultMsg(resultMsg)
                .resultDetail(drawingEvent.getResultDetail())
                .build();
    }

    private String getRandomMessageForScore(int score) {
        String messageKey = getMessageKeyForScore(score);
        String messagesString = messageSource.getMessage(messageKey, null, Locale.getDefault());
        List<String> messages = Arrays.asList(messagesString.split(","));
        return getRandomMessage(messages);
    }

    private String getMessageKeyForScore(int score) {
        if (score >= 90) {
            return "drawing.score.message.range.90_100";
        } else if (score >= 70) {
            return "drawing.score.message.range.70_90";
        } else if (score >= 50) {
            return "drawing.score.message.range.50_70";
        } else if (score >= 30) {
            return "drawing.score.message.range.30_50";
        } else if (score >= 10) {
            return "drawing.score.message.range.10_30";
        } else {
            return "drawing.score.message.range.0_10";
        }
    }

    private String getRandomMessage(List<String> messages) {
        int index = random.nextInt(messages.size());
        return messages.get(index);
    }

    @Transactional
    public DrawingTotalScoreDto getDrawingTotalScore(User authenticatedUser, SubEventRequest subEventRequest) {
        return drawingRank.getDrawingTotalScore(authenticatedUser, subEventRequest, RANK_COUNT);
    }

    @Transactional
    public void saveDrawImage(MultipartFile file, Long eventId, User authenticatedUser) {
        List<SubEvent> subEvents = subEventRepository.findByEventIdAndExecuteType(eventId, SubEventExecuteType.LOTTERY);

        SubEvent subEvent = subEvents
                .stream()
                .findFirst()
                .orElseThrow(() -> new SubEventNotFoundException());

        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(authenticatedUser.getId(), subEvent.getId())
                .orElseThrow(() -> new EventUserNotFoundException());

        String resultImgUrl = eventUser.getResultImgUrl();

        if(resultImgUrl != null) {
            amazonS3Client.deleteObject(bucket, BUCKET_DIR + resultImgUrl);
        }

        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(bucket, BUCKET_DIR + fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileUrl = amazonS3Client.getUrl(bucket, BUCKET_DIR + fileName).toString();

        eventUser.setResultImgUrl(fileUrl);
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "-" + originalFileName;
    }
}
