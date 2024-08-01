package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizLandResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository.QuizRepository;
import com.hyundai.softeer.backend.domain.subevent.exception.NoWinnerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
@Sql({"/sql/car.sql", "/sql/event.sql", "/sql/quiz.sql", "/sql/subEvent.sql"})
class QuizServiceTest {

    @Autowired
    QuizService quizService;

    @Autowired
    QuizRepository quizRepository;

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
    @DisplayName("getQuizLand 테스트: 정상인 경우")
    void getQuizLandCorrectTest(String time) {
        // given
        long eventId = 1L;
        setClock(time);

        // when
        QuizLandResponseDto quizLand = quizService.getQuizLand(eventId);

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
        }).isInstanceOf(QuizNotFoundException.class);
    }

    @Test
    @DisplayName("getQuiz 테스트: Quiz가 존재하는 경우")
    void getQuizTest() {
        // given
        long eventId = 1L;
        int problemSequence = 2;

        // when
        QuizResponseDto quiz = quizService.getQuiz(eventId, problemSequence);

        // then
        assertThat(quiz).isNotNull();
        assertThat(quiz.getProblem()).isEqualTo("산타페의 연비는?");
        assertThat(quiz.getHint()).isEqualTo("11 근처");
        assertThat(quiz.getInitConsonant()).isEqualTo("ㅅㅇㅈㅇ");
    }

    @Test
    @DisplayName("getQuiz 테스트: 퀴즈가 존재하지 않을 때")
    void getQuizNonProperParameterTest() {
        // given
        long eventId = 2L;
        int problemSequence = 2;

        // when, then
        Assertions.assertThatThrownBy(() -> {
            quizService.getQuiz(eventId, problemSequence);
        }).isInstanceOf(QuizNotFoundException.class);
    }

    @Test
    @DisplayName("quizSubmit 테스트: 퀴즈가 존재하지 않을 때")
    void quizSubmitNotExistQuizTest() {
        // given
        long subEventId = 4L;
        String answer = "minjun";

        // when, then
        Assertions.assertThatThrownBy(() -> {
            quizService.quizSubmit(subEventId, answer);
        }).isInstanceOf(QuizNotFoundException.class);
    }

    @Test
    @DisplayName("quizSubmit 테스트: 퀴즈를 틀렸을 때")
    void quizSubmitAnswerNotCorrectTest() {
        // given
        long subEventId = 1L;
        String answer = "10.9";

        // when, then
        Assertions.assertThatThrownBy(() -> {
                    quizService.quizSubmit(subEventId, answer);
                })
                .isInstanceOf(NoWinnerException.class)
                .hasMessage("정답이 아니에요.");
    }

    @Test
    @DisplayName("quizSumit 테스트: 퀴즈가 존재할 때")
    void quizSubmitOperatingTest() {
        // given
        long subEventId = 1L;
        String answer = "10.4";

        // when
        QuizSubmitResponseDto quizSubmitResponseDto = quizService.quizSubmit(subEventId, answer);
        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow();

        // then
        assertThat(quizSubmitResponseDto).isNotNull();
        assertThat(quizSubmitResponseDto.getIsCorrect()).isTrue();
        assertThat(quizSubmitResponseDto.getPrizeImgUrl()).isEqualTo("img2.com");
        assertThat(quiz.getWinners()).isEqualTo(3);
    }

    @Test
    @DisplayName("quizSubmit 테스트: 정해진 수의 당첨자 수가 넘었을 때")
    void quizSubmitEndEventTest() {
        // given
        long subEventId = 3L;
        String answer = "12.5";

        // when them
        Assertions.assertThatThrownBy(() -> {
                    quizService.quizSubmit(subEventId, answer);
                })
                .isInstanceOf(NoWinnerException.class)
                .hasMessage("선착순 이벤트가 끝났어요.");
    }
}
