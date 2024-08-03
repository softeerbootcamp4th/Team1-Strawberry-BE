package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service;

import com.hyundai.softeer.backend.domain.event.exception.EventNotWithinPeriodException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.*;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository.QuizRepository;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.global.jwt.OAuthProvider;
import com.hyundai.softeer.backend.global.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
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

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
@Sql(value = {"/sql/integration.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Slf4j
class QuizServiceTest {

    @Autowired
    QuizService quizService;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    SubEventRepository subEventRepository;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    Clock clock;

    User user = new User("alswnssl0528@naver.com", "김민준", "010-6860-6823", LocalDate.of(2024, 7, 24), OAuthProvider.NAVER);

    private void setClock(String time) {
        ReflectionTestUtils.setField(quizService, "clock", Clock.fixed(
                Instant.parse(time),
                ZoneId.of("Asia/Seoul")
        ));
        ReflectionTestUtils.setField(dateUtil, "clock", Clock.fixed(
                Instant.parse(time),
                ZoneId.of("Asia/Seoul")
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-25T15:00:00Z"
    })
    @DisplayName("getQuizLand 테스트: 현재 이벤트 진행 중임을 나타냄")
    void getQuizLandCorrectTest(String time) {
        setClock(time);
        // given
        long eventId = 1L;

        // when
        QuizLandResponseDto quizLand = quizService.getQuizLand(eventId);

        // then
        assertThat(quizLand).isNotNull();
        assertThat(quizLand.getValid()).isTrue();
        assertThat(quizLand.getProblem()).isEqualTo("산타페의 연비는?");
        assertThat(quizLand.getHint()).isEqualTo("10 근처");
        assertThat(quizLand.getQuizSequence()).isEqualTo(1);
        assertThat(quizLand.getRemainSecond()).isEqualTo(0L);
        assertThat(quizLand.getPrizeInfos()).containsExactly(
                new PrizeInfo(true, "스타벅스 상품권", "www.starbucks.com", 1, LocalDate.of(2024, 6, 25)),
                new PrizeInfo(true, "자전거", "www.bicycle.com", 2, LocalDate.of(2024, 6, 27)),
                new PrizeInfo(true, "아이폰", "www.apple.com", 3, LocalDate.of(2024, 6, 29))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-27T15:00:00Z"
    })
    @DisplayName("getQuizLand 테스트: 이벤트 기간 중이지만 선착순 경품이 모두 소진된 경우")
    void getQuizNotWinnerTest(String time) {
        setClock(time);
        // given
        long eventId = 1L;

        // when
        QuizLandResponseDto quizLand = quizService.getQuizLand(eventId);

        // then
        assertThat(quizLand).isNotNull();
        assertThat(quizLand.getValid()).isTrue();
        assertThat(quizLand.getProblem()).isEqualTo("산타페의 연비는?");
        assertThat(quizLand.getHint()).isEqualTo("11 근처");
        assertThat(quizLand.getQuizSequence()).isEqualTo(2);
        assertThat(quizLand.getRemainSecond()).isEqualTo(0L);
        assertThat(quizLand.getPrizeInfos()).containsExactly(
                new PrizeInfo(false, "스타벅스 상품권", "www.starbucks.com", 1, LocalDate.of(2024, 6, 25)),
                new PrizeInfo(false, "자전거", "www.bicycle.com", 2, LocalDate.of(2024, 6, 27)),
                new PrizeInfo(true, "아이폰", "www.apple.com", 3, LocalDate.of(2024, 6, 29))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-26T15:00:00Z"
    })
    @DisplayName("getQuizLand 테스트: 이벤트 시작 시간 이전인 경우")
    void getQuizLandBeforeStartQuizEvent(String time) {
        setClock(time);
        // given
        long eventId = 1L;

        // when
        QuizLandResponseDto quizLand = quizService.getQuizLand(eventId);
        SubEvent subEvent = subEventRepository.findById(2L)
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(quizLand).isNotNull();
        assertThat(quizLand.getValid()).isTrue();
        assertThat(quizLand.getProblem()).isEqualTo("산타페의 연비는?");
        assertThat(quizLand.getHint()).isEqualTo("11 근처");
        assertThat(quizLand.getQuizSequence()).isEqualTo(2);
        assertThat(quizLand.getRemainSecond()).isEqualTo(dateUtil.startBetweenCurrentDiff(subEvent));
        assertThat(quizLand.getPrizeInfos()).containsExactly(
                new PrizeInfo(false, "스타벅스 상품권", "www.starbucks.com", 1, LocalDate.of(2024, 6, 25)),
                new PrizeInfo(true, "자전거", "www.bicycle.com", 2, LocalDate.of(2024, 6, 27)),
                new PrizeInfo(true, "아이폰", "www.apple.com", 3, LocalDate.of(2024, 6, 29))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-30T19:00:00Z"
    })
    @DisplayName("getQuizLand 테스트: 퀴즈 이벤트가 모두 종료된 경우")
    void getQuizLandEventEnd(String time) {
        setClock(time);
        // given
        long eventId = 1L;

        // when
        QuizLandResponseDto quizLand = quizService.getQuizLand(eventId);

        // then
        assertThat(quizLand).isNotNull();
        assertThat(quizLand.getValid()).isFalse();
        assertThat(quizLand.getProblem()).isNull();
        assertThat(quizLand.getHint()).isNull();
        assertThat(quizLand.getQuizSequence()).isNull();
        assertThat(quizLand.getRemainSecond()).isNull();
        assertThat(quizLand.getPrizeInfos()).containsExactly(
                new PrizeInfo(false, "스타벅스 상품권", "www.starbucks.com", 1, LocalDate.of(2024, 6, 25)),
                new PrizeInfo(false, "자전거", "www.bicycle.com", 2, LocalDate.of(2024, 6, 27)),
                new PrizeInfo(false, "아이폰", "www.apple.com", 3, LocalDate.of(2024, 6, 29))
        );
        assertThat(quizLand.getBannerImg()).isEqualTo("www.banner1.com");
        assertThat(quizLand.getEventImg()).isEqualTo("www.event1.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2025-06-30T19:00:00Z"
    })
    @DisplayName("getQuizLand 테스트: 이벤트 기간이 아예 아닌 경우")
    void getQuizLandEventNotInPeriod(String time) {
        setClock(time);
        // given
        long eventId = 1L;

        // when
        Assertions.assertThatThrownBy(() -> {
            quizService.getQuizLand(eventId);
        })
                .isInstanceOf(EventNotWithinPeriodException.class)
                .hasMessage("이벤트 기간이 아닙니다.");
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
        QuizSubmitRequest quizSubmitRequest = new QuizSubmitRequest(answer, subEventId);

        // when, then
        Assertions.assertThatThrownBy(() -> {
            quizService.quizSubmit(quizSubmitRequest, user);
        }).isInstanceOf(SubEventNotFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-25T19:00:00Z"
    })
    @DisplayName("quizSubmit 테스트: 퀴즈를 틀렸을 때")
    void quizSubmitAnswerNotCorrectTest(String time) {
        // given
        setClock(time);
        long subEventId = 1L;
        String answer = "10.9";
        QuizSubmitRequest quizSubmitRequest = new QuizSubmitRequest(answer, subEventId);

        // when
        QuizSubmitResponseDto quizSubmitResponseDto = quizService.quizSubmit(quizSubmitRequest, user);

        // then
        assertThat(quizSubmitResponseDto).isNotNull();
        assertThat(quizSubmitResponseDto.getIsCorrect()).isFalse();
        assertThat(quizSubmitResponseDto.getIsWinner()).isFalse();
        assertThat(quizSubmitResponseDto.getPrizeImgUrl()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-25T19:00:00Z"
    })
    @DisplayName("quizSubmit 테스트: 퀴즈를 맞추고 선착순안에 들었을 때")
    void quizSubmitOperatingTest(String time) {
        // given
        setClock(time);
        long subEventId = 1L;
        String answer = "10.4";
        QuizSubmitRequest quizSubmitRequest = new QuizSubmitRequest(answer, subEventId);

        // when
        QuizSubmitResponseDto quizSubmitResponseDto = quizService.quizSubmit(quizSubmitRequest, user);

        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow();

        // then
        assertThat(quizSubmitResponseDto).isNotNull();
        assertThat(quizSubmitResponseDto.getIsCorrect()).isTrue();
        assertThat(quizSubmitResponseDto.getIsWinner()).isTrue();
        assertThat(quiz.getWinnerCount()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-27T19:00:00Z"
    })
    @DisplayName("quizSubmit 테스트: 퀴즈는 맞췄지만 선착순에 들지 못한 경우")
    void quizSubmitCorrectButNotWinner(String time) {
        // given
        setClock(time);
        long subEventId = 2L;
        String answer = "11.5";
        QuizSubmitRequest quizSubmitRequest = new QuizSubmitRequest(answer, subEventId);

        // when
        QuizSubmitResponseDto quizSubmitResponseDto = quizService.quizSubmit(quizSubmitRequest, user);

        Quiz quiz = quizRepository.findBySubEventId(subEventId)
                .orElseThrow();

        // then
        assertThat(quizSubmitResponseDto).isNotNull();
        assertThat(quizSubmitResponseDto.getIsCorrect()).isTrue();
        assertThat(quizSubmitResponseDto.getIsWinner()).isFalse();
        assertThat(quiz.getWinnerCount()).isEqualTo(3);
    }

}
