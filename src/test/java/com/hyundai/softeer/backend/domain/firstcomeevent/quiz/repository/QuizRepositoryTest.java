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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/sql/quiz.sql"})
@Slf4j
public class QuizRepositoryTest {

    @Autowired
    private QuizRepository quizRepository;

    @Test
    @DisplayName("findBySubEventIdAndSequence 테스트:" +
            "정상 작동 테스트")
    void findQuizTest() {
        // given
        Long subEventId = 1L;
        int sequence = 1;

        // when
        Quiz quiz = quizRepository.findBySubEventIdAndSequence(subEventId, sequence);

        // then
        assertThat(quiz).isNotNull();
        assertThat(quiz.getAnchor()).isEqualTo("#sub1");
    }

    @Test
    @DisplayName("findBySubEventIdAndSequence 테스트:" +
    "결과 값이 존재하지 않을 때")
    void findQuizNullTest() {
        // given
        Long subEventId = 1L;
        int sequence = 2;

        // when
        Quiz quiz = quizRepository.findBySubEventIdAndSequence(subEventId, sequence);

        // then
        assertThat(quiz).isNull();
    }
}