package com.hyundai.softeer.backend.domain.firstcome.quiz.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.exception.EventNotWithinPeriodException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.*;
import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.firstcome.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.firstcome.quiz.repository.QuizFirstComeRepository;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotWithinPeriodException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import com.hyundai.softeer.backend.global.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizFirstComeService {
    private final QuizFirstComeRepository quizFirstComeRepository;
    private final SubEventRepository subEventRepository;
    private final WinnerRepository winnerRepository;
    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;
    private final UserRepository userRepository;
    private final DateUtil dateUtil;
    private final Clock clock;

    /**
     * 퀴즈를 가져오기 위한 비즈니스 로직
     * <p>
     * 예외
     * 퀴즈가 없는 경우: QuizNotFoundException
     *
     * @param eventId  이벤트 id
     * @param sequence 퀴즈 번호
     * @return QuizResponseDto
     */
    @Transactional(readOnly = true)
    public QuizFirstComeResponseDto getQuiz(QuizFirstComeRequest quizFirstComeRequest) {
        Long subEventId = quizFirstComeRequest.getSubEventId();

        QuizFirstCome quizFirstCome = quizFirstComeRepository.findBySubEventId(subEventId)
                .orElseThrow(() -> new QuizNotFoundException());

        SubEvent subEvent = subEventRepository.findById(subEventId)
                .orElseThrow(() -> new SubEventNotFoundException());

        if (dateUtil.isNotWithinSubEventPeriod(subEvent)) {
            throw new SubEventNotWithinPeriodException();
        }

        return QuizFirstComeResponseDto.builder()
                .subEventId(subEventId)
                .overview(quizFirstCome.getOverview())
                .problem(quizFirstCome.getProblem())
                .carInfo(quizFirstCome.getCarInfo())
                .hint(quizFirstCome.getHint())
                .initConsonant(quizFirstCome.getInitConsonant())
                .build();
    }

    /**
     * 랜딩 페이지 정보를 반환하는 비즈니스 로직
     * 가장 가까운 퀴즈 이벤트를 찾아서 (퀴즈 이벤트 전, 퀴즈 이벤트 후) 처리
     * <p>
     * 예외
     * 이벤트가 존재하지 않는 경우: NotExistEventException
     * 이벤트 기간이 아닌 경우   : EventNotWithinPeriodException
     * 퀴즈가 존재하지 않는 경우  : QuizNotFoundException
     *
     * @param eventId 이벤트 id
     * @return QuizLandResponseDto
     */
    @Transactional(readOnly = true)
    public QuizFirstComeLandResponseDto getQuizLand(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        if (dateUtil.eventNotWithinPeriod(event)) {
            throw new EventNotWithinPeriodException();
        }

        List<SubEvent> subEvents = subEventRepository.findByEventIdAndExecuteType(eventId, SubEventExecuteType.FIRSTCOME);

        SubEvent subEvent = dateUtil.findClosestSubEvent(subEvents);

        if (subEvent == null) {
            return getPrizesWithEventEnd(subEvents);
        }

        QuizFirstCome quizFirstCome = quizFirstComeRepository.findBySubEventId(subEvent.getId())
                .orElseThrow(() -> new QuizNotFoundException());

        Integer winners = quizFirstCome.getWinners();

        List<PrizeInfo> prizes = getPrizes(subEvents, winners, quizFirstCome);

        return QuizFirstComeLandResponseDto.builder()
                .valid(true)
                .bannerImg(subEvent.getBannerImgUrl())
                .eventImg(subEvent.getEventImgUrls())
                .remainSecond(dateUtil.startBetweenCurrentDiff(subEvent))
                .problem(quizFirstCome.getProblem())
                .overview(quizFirstCome.getOverview())
                .hint(quizFirstCome.getHint())
                .anchor(quizFirstCome.getAnchor())
                .subEventId(quizFirstCome.getSubEventId())
                .prizeInfos(prizes)
                .startAt(subEvent.getStartAt())
                .endAt(subEvent.getEndAt())
                .lastQuizNumber(subEvents.size())
                .build();
    }

    private List<PrizeInfo> getPrizes(List<SubEvent> subEvents, int winners, QuizFirstCome currentQuizFirstCome) {
        LocalDateTime current = LocalDateTime.now(clock);
        List<PrizeInfo> prizeInfos = subEvents.stream()
                .filter((subEvent -> subEvent.getEventType().equals(SubEventType.QUIZ)))
                .map((subEvent) -> {
                    QuizFirstCome quizFirstCome = quizFirstComeRepository.findBySubEventId(subEvent.getId())
                            .orElseThrow(() -> new QuizNotFoundException());

                    Prize prize = quizFirstCome.getPrize();

                    boolean isValidPrize = (quizFirstCome.getWinnerCount() != winners
                            && quizFirstCome.getSequence() >= currentQuizFirstCome.getSequence())
                            || current.isBefore(subEvent.getStartAt());

                    return new PrizeInfo(
                            isValidPrize,
                            prize.getProductName(),
                            prize.getPrizeImgUrl(),
                            quizFirstCome.getSequence(),
                            subEvent.getStartAt().toLocalDate());
                })
                .collect(Collectors.toList());

        Collections.sort(prizeInfos, Comparator.comparingInt(PrizeInfo::getQuizSequence));

        return prizeInfos;
    }

    private QuizFirstComeLandResponseDto getPrizesWithEventEnd(List<SubEvent> subEvents) {
        List<PrizeInfo> prizeInfos = subEvents.stream()
                .map((subEvent) -> {
                    QuizFirstCome quizFirstCome = quizFirstComeRepository.findBySubEventId(subEvent.getId())
                            .orElseThrow(() -> new QuizNotFoundException());
                    Prize prize = quizFirstCome.getPrize();
                    return new PrizeInfo(
                            false,
                            prize.getProductName(),
                            prize.getPrizeImgUrl(),
                            quizFirstCome.getSequence(),
                            subEvent.getStartAt().toLocalDate());
                })
                .collect(Collectors.toList());

        Collections.sort(prizeInfos, Comparator.comparingInt(PrizeInfo::getQuizSequence));

        SubEvent subEvent = subEvents.get(0);

        QuizFirstCome quizFirstCome = quizFirstComeRepository.findById(subEvent.getId())
                .orElseThrow(() -> new QuizNotFoundException());

        return QuizFirstComeLandResponseDto.builder()
                .bannerImg(subEvent.getBannerImgUrl())
                .eventImg(subEvent.getEventImgUrls())
                .prizeInfos(prizeInfos)
                .valid(false)
                .winners(quizFirstCome.getWinners())
                .build();
    }

    /**
     * 퀴즈 제출을 처리하는 비즈니스 로직
     * <p>
     * 예외
     * SubEvent가 존재하지 않을 때: SubEventNotFoundException
     *
     * @param quizFirstComeSubmitRequest
     * @param user
     * @return
     */
    @Transactional
    public QuizFirstComeSubmitResponseDto quizSubmit(QuizFirstComeSubmitRequest quizFirstComeSubmitRequest, User user) {

        Long subEventId = quizFirstComeSubmitRequest.getSubEventId();
        String answer = quizFirstComeSubmitRequest.getAnswer();

        SubEvent subEvent = subEventRepository.findById(subEventId)
                .orElseThrow(() -> new SubEventNotFoundException());

        if (dateUtil.isNotWithinSubEventPeriod(subEvent)) {
            throw new SubEventNotWithinPeriodException();
        }

        QuizFirstCome quizFirstCome = quizFirstComeRepository.findBySubEventId(subEventId)
                .orElseThrow(() -> new SubEventNotFoundException());

        if (!quizFirstCome.getAnswer().equals(answer)) {
            return QuizFirstComeSubmitResponseDto.notCorrect();
        }

        int winners = quizFirstCome.getWinners();
        int winnerCount = quizFirstCome.getWinnerCount();

        if (winnerCount >= winners) {
            return QuizFirstComeSubmitResponseDto.correctBut();
        }

        Prize prize = quizFirstCome.getPrize();

        quizFirstCome.setWinnerCount(winnerCount + 1);

        if(isParticipanted(user.getId(), subEventId).isEmpty()) {
            QuizFirstComeSubmitResponseDto.alreadyParticipant();
        }

        EventUser eventUser = EventUser
                .builder()
                .user(userRepository.getReferenceById(user.getId()))
                .subEvent(subEventRepository.getReferenceById(subEventId))
                .chance(0)
                .build();

        eventUserRepository.save(eventUser);

        Winner winner = new Winner();
        winner.setPrize(prize);
        winner.setSubEvent(subEvent);
        winner.setUser(user);
        winnerRepository.save(winner);

        return QuizFirstComeSubmitResponseDto.winner(prize.getPrizeImgUrl());
    }

    private Optional<Winner> isParticipanted(long userId, long subEventId) {
        return winnerRepository.findByUserIdAndSubEventId(userId, subEventId);
    }
}
