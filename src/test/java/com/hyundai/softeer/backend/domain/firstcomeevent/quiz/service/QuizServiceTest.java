package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.GetQuizResponseDto;

import com.hyundai.softeer.backend.global.exception.NoContentException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
@Sql({"/sql/car.sql", "/sql/event.sql", "/sql/quiz.sql", "/sql/subEvent.sql"})
class QuizServiceTest {

    @Autowired
    QuizService quizService;

    @Autowired
    Clock clock;

    private void setClock(String timeString) {
        Clock fixedClock = Clock.fixed(
                Instant.parse(timeString),
                ZoneId.of("Asia/Seoul")
        );
        ReflectionTestUtils.setField(quizService, "clock", fixedClock);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-25T12:30:30.00Z"
    })
    @DisplayName("getQuiz 테스트: 정상인 경우")
    void getQuizTest(String timeString) {
        // given
        Long subEventId = 1L;
        int sequence = 1;
        setClock(timeString);
        // when
        GetQuizResponseDto responseDto = quizService.getQuiz(subEventId, sequence);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAnchor()).isEqualTo("#sub1");
        assertThat(responseDto.getBannerImg()).isEqualTo("www.banner1.com");
        assertThat(responseDto.getHint()).isEqualTo("10 근처");
        assertThat(responseDto.getPrizes()).isEqualTo("{1:1, 2:2, 3:3}");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-25T12:30:30.00Z"
    })
    @DisplayName("getQuizLand 테스트: 정상인 경우")
    void getQuizLandCorrectTest(String time) {
        // given
        long eventId = 1L;
        setClock(time);

        // when
        GetQuizResponseDto quizLand = quizService.getQuizLand(eventId);

        // then
        assertThat(quizLand).isNotNull();
        assertThat(quizLand.getProblem()).isEqualTo("산타페의 연비는?");
        assertThat(quizLand.getHint()).isEqualTo("10 근처");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2028-06-25T12:30:30.00Z"
    })
    @DisplayName("getQuizLand 테스트: 이 기간에 하는 퀴즈가 없는 경우")
    void getQuizLandWrongTest() {
        // given
        long eventId = 1L;

        // when, then
        Assertions.assertThatThrownBy(() -> {
            quizService.getQuizLand(eventId);
        }).isInstanceOf(NoContentException.class);
    }
}