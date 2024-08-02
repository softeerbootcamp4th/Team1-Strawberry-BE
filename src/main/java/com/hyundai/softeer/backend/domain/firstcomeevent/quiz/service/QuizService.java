package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotWithinPeriodException;
import com.hyundai.softeer.backend.domain.event.exception.NotExistEventException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.*;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.NotExistQuizException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository.QuizRepository;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.prize.exception.NotExistPrizeException;
import com.hyundai.softeer.backend.domain.prize.repository.PrizeRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotWithinPeriodException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import com.hyundai.softeer.backend.global.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final SubEventRepository subEventRepository;
    private final WinnerRepository winnerRepository;
    private final PrizeRepository prizeRepository;
    private final EventRepository eventRepository;
    private final DateUtil dateUtil;
    private final Clock clock;

    @Transactional(readOnly = true)
    public QuizResponseDto getQuiz(Long eventId, Integer sequence) {
        Optional<Quiz> optionalQuiz = quizRepository.findQuiz(eventId, sequence);

        Quiz quiz = optionalQuiz
                .orElseThrow(() -> new QuizNotFoundException());

        return QuizResponseDto.builder()
                .subEventId(quiz.getSubEventId())
                .overview(quiz.getOverview())
                .problem(quiz.getProblem())
                .carInfo(quiz.getCarInfo())
                .hint(quiz.getHint())
                .initConsonant(quiz.getInitConsonant())
                .build();
    }

    @Transactional(readOnly = true)
    public QuizLandResponseDto getQuizLand(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotExistEventException());

        if(eventNotWithinPeriod(event)) {
            throw new EventNotWithinPeriodException();
        }

        List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);

        SubEvent subEvent = findClosestSubEvent(subEvents);

        if (subEvent == null) {
            return getPrizesWithEventEnd(subEvents);
        }

        Quiz quiz = quizRepository.findBySubEventId(subEvent.getId())
                .orElseThrow(() -> new QuizNotFoundException());

        Integer winners = quiz.getWinners();

        List<PrizeInfo> prizes = getPrizes(subEvents, winners, quiz);

        return QuizLandResponseDto.builder()
                .valid(true)
                .bannerImg(subEvent.getBannerUrl())
                .eventImg(subEvent.getEventImgUrl())
                .remainSecond(dateUtil.startBetweenCurrentDiff(subEvent))
                .problem(quiz.getProblem())
                .overview(quiz.getOverview())
                .hint(quiz.getHint())
                .anchor(quiz.getAnchor())
                .quizSequence(quiz.getSequence())
                .prizeInfos(prizes)
                .startAt(subEvent.getStartAt())
                .endAt(subEvent.getEndAt())
                .build();
    }

    private List<PrizeInfo> getPrizes(List<SubEvent> subEvents, int winners, Quiz currentQuiz) {
        LocalDateTime current = LocalDateTime.now(clock);
        List<PrizeInfo> prizeInfos = subEvents.stream()
                .map((subEvent) -> {
                    Quiz quiz = quizRepository.findBySubEventId(subEvent.getId())
                            .orElseThrow(() -> new NotExistQuizException());

                    Prize prize = quiz.getPrize();

                    boolean isValidPrize = (quiz.getWinnerCount() != winners
                            && quiz.getSequence() >= currentQuiz.getSequence())
                            || current.isBefore(subEvent.getStartAt());

                    return new PrizeInfo(
                            isValidPrize,
                            prize.getProductName(),
                            prize.getPrizeImgUrl(),
                            quiz.getSequence(),
                            subEvent.getStartAt().toLocalDate());
                })
                .collect(Collectors.toList());
        Collections.sort(prizeInfos, Comparator.comparingInt(PrizeInfo::getQuizSequence));
        return prizeInfos;
    }

    private QuizLandResponseDto getPrizesWithEventEnd(List<SubEvent> subEvents) {
        List<PrizeInfo> prizeInfos = subEvents.stream()
                .map((subEvent) -> {
                    Quiz quiz = quizRepository.findBySubEventId(subEvent.getId())
                            .orElseThrow(() -> new NotExistQuizException());
                    Prize prize = quiz.getPrize();
                    return new PrizeInfo(
                            false,
                            prize.getProductName(),
                            prize.getPrizeImgUrl(),
                            quiz.getSequence(),
                            subEvent.getStartAt().toLocalDate());
                })
                .collect(Collectors.toList());
        Collections.sort(prizeInfos, Comparator.comparingInt(PrizeInfo::getQuizSequence));

        SubEvent subEvent = subEvents.get(0);
        Quiz quiz = quizRepository.findById(subEvent.getId())
                .orElseThrow(() -> new QuizNotFoundException());

        return QuizLandResponseDto.builder()
                .bannerImg(subEvent.getBannerUrl())
                .eventImg(subEvent.getEventImgUrl())
                .prizeInfos(prizeInfos)
                .valid(false)
                .winners(quiz.getWinners())
                .build();
    }

    private boolean eventNotWithinPeriod(Event event) {
        LocalDateTime current = LocalDateTime.now(clock);
        return current.isBefore(event.getStartAt()) || current.isAfter(event.getEndAt());
    }

    @Transactional
    public QuizSubmitResponseDto quizSubmit(QuizSubmitRequest quizSubmitRequest, User user) {

        Long subEventId = quizSubmitRequest.getSubEventId();
        String answer = quizSubmitRequest.getAnswer();

        SubEvent subEvent = subEventRepository.findById(subEventId)
                .orElseThrow(() -> new SubEventNotFoundException());

        if(dateUtil.isNotWithinSubEventPeriod(subEvent)) {
            throw new SubEventNotWithinPeriodException();
        }

        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow(() -> new SubEventNotFoundException());

        if (!quiz.getAnswer().equals(answer)) {
            return QuizSubmitResponseDto.notCorrect();
        }

        int winners = quiz.getWinners();
        int winnerCount = quiz.getWinnerCount();

        log.error("asdf = {},, {}", winners, winnerCount);

        if(winnerCount >= winners) {
           return QuizSubmitResponseDto.correctBut();
        }

        Prize prize = quiz.getPrize();

        quiz.setWinnerCount(winnerCount + 1);

        Winner winner = new Winner();
        winner.setPrize(prize);
        winner.setSubEvent(subEvent);
        winner.setUser(user);
        winnerRepository.save(winner);

        return QuizSubmitResponseDto.winner(prize.getPrizeImgUrl());
    }

    private SubEvent findClosestSubEvent(List<SubEvent> subEvents) {
        LocalDateTime current = LocalDateTime.now(clock);
        SubEvent closetSubEvent = null;
        for(SubEvent subEvent: subEvents) {
            if(subEvent.getEventType().equals(SubEventType.DRAWING)) continue;

            if(dateUtil.isWithinSubEventPeriod(subEvent)) {
                return subEvent;
            }

            if(current.isBefore(subEvent.getEndAt())) {
                return subEvent;
            }
        }
        return closetSubEvent;
    }
}
