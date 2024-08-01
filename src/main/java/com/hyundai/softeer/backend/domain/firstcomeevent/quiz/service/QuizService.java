package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizLandResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.JsonParseException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository.QuizRepository;
import com.hyundai.softeer.backend.domain.subevent.dto.WinnerInfo;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.exception.NoWinnerException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.global.utils.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final SubEventRepository subEventRepository;
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
        List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);
        SubEvent subEvent = findEventByDateTime(subEvents);

        if (subEvent == null) {
            throw new QuizNotFoundException();
        }

        Optional<Quiz> optionalQuiz = quizRepository.findBySubEventId(subEvent.getId());

        Quiz quiz = optionalQuiz
                .orElseThrow(() -> new QuizNotFoundException());

        return QuizLandResponseDto.builder()
                .bannerImg(subEvent.getBannerUrl())
                .eventImg(subEvent.getEventImgUrl())
                .startTime(subEvent.getStartAt())
                .endTime(subEvent.getEndAt())
                .serverTime(LocalDateTime.now())
                .problem(quiz.getProblem())
                .overview(quiz.getOverview())
                .hint(quiz.getHint())
                .anchor(quiz.getAnchor())
                .prizes(quiz.getWinners_meta())
                .build();
    }

    @Transactional
    public QuizSubmitResponseDto quizSubmit(Long subEventId, String answer) {
        Optional<Quiz> optionalQuiz = quizRepository.findBySubEventId(subEventId);

        Quiz quiz = optionalQuiz
                .orElseThrow(() -> new QuizNotFoundException());

        if (!quiz.getAnswer().equals(answer)) {
            throw new NoWinnerException("정답이 아니에요.");
        }

        String winnersMeta = quiz.getWinners_meta();
        Integer winners = quiz.getWinners();

        try {
            Map<Integer, WinnerInfo> integerWinnerInfoMap = ParseUtil.parseWinnersMeta(winnersMeta);

            WinnerInfo winnerInfo = findPrize(integerWinnerInfoMap, winners)
                    .orElseThrow(() -> new NoWinnerException("선착순 이벤트가 끝났어요."));
            quiz.setWinners(winners + 1);
            return new QuizSubmitResponseDto(true, winnerInfo.getPrizeImgUrl());
        } catch (JsonProcessingException e) {
            throw new JsonParseException();
        }
    }

    private Optional<WinnerInfo> findPrize(Map<Integer, WinnerInfo> integerWinnerInfoMap, int winners) {
        int accumulate = 0;
        for (Integer key : integerWinnerInfoMap.keySet()) {
            WinnerInfo winnerInfo = integerWinnerInfoMap.get(key);
            accumulate += winnerInfo.getWinnerCount();

            if (winners < accumulate) {
                return Optional.of(winnerInfo);
            }
        }
        return Optional.empty();
    }

    /**
     * A-2. 현재 시간 기준 가장 가까운 선착순 이벤트 오픈 시간까지 남은 시간을 표시한다.
     *
     * @param subEvents
     * @return
     */
    private SubEvent findEventByDateTime(List<SubEvent> subEvents) {
        for (SubEvent subEvent : subEvents) {
            LocalDateTime startAt = subEvent.getStartAt();
            LocalDateTime endAt = subEvent.getEndAt();
            LocalDateTime current = LocalDateTime.now(clock);
            if (current.isAfter(startAt) && current.isBefore(endAt)) {
                return subEvent;
            }
        }
        return null;
    }
}
