package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/sql/car.sql", "/sql/event.sql", "/sql/subEvent.sql", "/sql/quiz.sql"})
@Slf4j
public class QuizRepositoryTest {

    @Autowired
    private QuizRepository quizRepository;

    @Test
    @DisplayName("퀴즈를 탐색하는 메서드 테스트: 정상 작동 시")
    void findQuizTest() {
        // given
        Long eventId = 1L;
        int sequence = 2;

        // when
        Optional<Quiz> quiz = quizRepository.findQuiz(eventId, sequence);


        // then
        assertThat(quiz.isPresent()).isTrue();
        Quiz _quiz = quiz.get();
        assertThat(_quiz.getAnchor()).isEqualTo("#sub2");
    }


    @Test
    @DisplayName("퀴즈를 탐색하는 메서드 테스트: 존재하지 않는 퀴즈일 때")
    void quizNotExistTest() {
        // given
        Long eventId = 1L;
        int sequence = 4;

        // when
        Optional<Quiz> quiz = quizRepository.findQuiz(eventId, sequence);

        // then
        assertThat(quiz.isPresent()).isFalse();
    }

    @Test
    @DisplayName("당첨자 수를 1 증가 시키는 메서드")
    void increaseOneTest() {
        // given
        Long subEventId = 1L;

        // when
        quizRepository.incrementOneWinners(subEventId);
        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow();

        // then
        assertThat(quiz.getWinners()).isEqualTo(3);
    }

    @Test
    @DisplayName("당첨자 수를 1 증가 시키는 메서드: 영속성 컨텍스트를 활용")
    void increaseOneUsingContextTest() {
        // given
        Long subEventId = 1L;

        // when
        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow();
        quiz.setWinners(quiz.getWinners() + 1);

        // then
        Quiz updatedQuiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow();

        assertThat(updatedQuiz.getWinners()).isEqualTo(3);
    }
}
