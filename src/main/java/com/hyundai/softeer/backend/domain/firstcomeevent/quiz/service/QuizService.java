package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizLandResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository.QuizRepository;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.global.exception.NoContentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
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

        if(optionalQuiz.isEmpty()) {
           throw new NoContentException("퀴즈가 존재하지 않습니다.");
        }

        Quiz quiz = optionalQuiz.get();

        return QuizResponseDto.builder()
                .problem(quiz.getProblem())
                .hint(quiz.getHint())
                .initConsonant(quiz.getInitConsonant())
                .build();
    }

    @Transactional(readOnly = true)
    public QuizLandResponseDto getQuizLand(Long eventId) {
        List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);
        SubEvent subEvent = findEventByDateTime(subEvents);

        if(subEvent == null) {
            throw new NoContentException("해당하는 quiz 이벤트가 존재하지 않습니다.");
        }

        Quiz quiz = quizRepository.findBySubEventId(subEvent.getId());

        return QuizLandResponseDto.builder()
                .bannerImg(subEvent.getBannerUrl())
                .eventImg(subEvent.getEventImgUrl())
                .startTime(subEvent.getStartAt())
                .endTime(subEvent.getEndAt())
                .serverTime(LocalDateTime.now())
                .problem(quiz.getProblem())
                .hint(quiz.getHint())
                .anchor(quiz.getAnchor())
                .prizes(quiz.getWinners_meta())
                .build();
    }

    private SubEvent findEventByDateTime(List<SubEvent> subEvents) {
        for(SubEvent subEvent: subEvents) {
            LocalDateTime startAt = subEvent.getStartAt();
            LocalDateTime endAt = subEvent.getEndAt();
            LocalDateTime current = LocalDateTime.now(clock);
            if(current.isAfter(startAt) && current.isBefore(endAt)) {
               return subEvent;
            }
        }
        return null;
    }
}
