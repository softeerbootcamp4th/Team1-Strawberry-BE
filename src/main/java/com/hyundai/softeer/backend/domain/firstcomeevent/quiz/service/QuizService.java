package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.exception.EventNotWithinPeriodException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.*;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository.QuizRepository;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
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
import java.time.Instant;
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
    private final EventRepository eventRepository;
    private final DateUtil dateUtil;
    private final Clock clock;

    /**
     * 퀴즈를 가져오기 위한 비즈니스 로직
     *
     * 예외
     * 퀴즈가 없는 경우: QuizNotFoundException
     *
     * @param eventId 이벤트 id
     * @param sequence 퀴즈 번호
     *
     * @return QuizResponseDto
     */
    @Transactional(readOnly = true)
    public QuizResponseDto getQuiz(QuizRequest quizRequest) {
        Long subEventId = quizRequest.getSubEventId();

        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow(() -> new QuizNotFoundException());

        SubEvent subEvent = subEventRepository.findById(subEventId)
                .orElseThrow(() -> new SubEventNotFoundException());

        if(dateUtil.isNotWithinSubEventPeriod(subEvent)) {
            throw new SubEventNotWithinPeriodException();
        }

        return QuizResponseDto.builder()
                .subEventId(subEventId)
                .overview(quiz.getOverview())
                .problem(quiz.getProblem())
                .carInfo(quiz.getCarInfo())
                .hint(quiz.getHint())
                .initConsonant(quiz.getInitConsonant())
                .build();
    }

    /**
     * 랜딩 페이지 정보를 반환하는 비즈니스 로직
     * 가장 가까운 퀴즈 이벤트를 찾아서 (퀴즈 이벤트 전, 퀴즈 이벤트 후) 처리
     *
     * 예외
     * 이벤트가 존재하지 않는 경우: NotExistEventException
     * 이벤트 기간이 아닌 경우   : EventNotWithinPeriodException
     * 퀴즈가 존재하지 않는 경우  : QuizNotFoundException
     *
     *
     * @param eventId 이벤트 id
     * @return QuizLandResponseDto
     */
    @Transactional(readOnly = true)
    public QuizLandResponseDto getQuizLand(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        if(dateUtil.eventNotWithinPeriod(event)) {
            throw new EventNotWithinPeriodException();
        }

        List<SubEvent> subEvents = subEventRepository.findByEventIdAndExecuteType(eventId, SubEventExecuteType.FIRSTCOME);

        SubEvent subEvent = dateUtil.findClosestSubEvent(subEvents);

        if (subEvent == null) {
            return getPrizesWithEventEnd(subEvents);
        }

        Quiz quiz = quizRepository.findBySubEventId(subEvent.getId())
                .orElseThrow(() -> new QuizNotFoundException());

        Integer winners = quiz.getWinners();

        List<PrizeInfo> prizes = getPrizes(subEvents, winners, quiz);

        return QuizLandResponseDto.builder()
                .valid(true)
                .bannerImg(subEvent.getBannerImgUrl())
                .eventImg(subEvent.getEventImgUrls())
                .remainSecond(dateUtil.startBetweenCurrentDiff(subEvent))
                .problem(quiz.getProblem())
                .overview(quiz.getOverview())
                .hint(quiz.getHint())
                .anchor(quiz.getAnchor())
                .subEventId(quiz.getSubEventId())
                .prizeInfos(prizes)
                .startAt(subEvent.getStartAt())
                .endAt(subEvent.getEndAt())
                .lastQuizNumber(subEvents.size())
                .build();
    }

    private List<PrizeInfo> getPrizes(List<SubEvent> subEvents, int winners, Quiz currentQuiz) {
        LocalDateTime current = LocalDateTime.now(clock);
        List<PrizeInfo> prizeInfos = subEvents.stream()
                .filter((subEvent -> subEvent.getEventType().equals(SubEventType.QUIZ)))
                .map((subEvent) -> {
                    Quiz quiz = quizRepository.findBySubEventId(subEvent.getId())
                            .orElseThrow(() -> new QuizNotFoundException());

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
                            .orElseThrow(() -> new QuizNotFoundException());
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
                .bannerImg(subEvent.getBannerImgUrl())
                .eventImg(subEvent.getEventImgUrls())
                .prizeInfos(prizeInfos)
                .valid(false)
                .winners(quiz.getWinners())
                .build();
    }

    /**
     * 퀴즈 제출을 처리하는 비즈니스 로직
     *
     * 예외
     * SubEvent가 존재하지 않을 때: SubEventNotFoundException
     *
     * @param quizSubmitRequest
     * @param user
     * @return
     */
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
}
