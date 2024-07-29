package com.hyundai.softeer.backend.domain.integration;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.service.QuizService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
@Sql({"/sql/car.sql", "/sql/event.sql", "/sql/quiz.sql", "/sql/subEvent.sql"})
public class QuizApiTest {

    @Autowired
    MockMvc mockMvc;

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

    @Test
    @DisplayName("이벤트가 존재하지 않거나 현재 시간의 퀴즈 이벤트가 존재하지 않을 때")
    void quizApiTest() throws Exception {
        // given
        String url = "/api/v1/quiz/land?eventId=2";

        // when
        ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("[/api/v1/quiz/land] url 요청 시 query parameter(eventId)를 보내지 않았을 때")
    void quizApiNonEventIdTest() throws Exception {
        // given
        String url = "/api/v1/quiz/land";

        // when
        ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-06-25T12:30:30.00Z"
    })
    @DisplayName("[/api/v1/quiz/land] 퀴즈 이벤트가 존재하는 시간일 때")
    void quizApiSuccessTest(String time) throws Exception {
        // given
        String url = "/api/v1/quiz/land?eventId=1";
        setClock(time);

        // when
        ResultActions result = mockMvc.perform(get(url));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.hint").value("10 근처"))
                .andExpect(jsonPath("$.problem").value("산타페의 연비는?"));
    }

    @Test
    @DisplayName("[/api/v1/quiz] 정상 요청인 경우")
    void getQuizTest() throws Exception {
        // given
        String url = "/api/v1/quiz?eventId=1&problemNumber=2";

        // when
        ResultActions result = mockMvc.perform(get(url));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.problem").value("산타페의 연비는?"))
                .andExpect(jsonPath("$.hint").value("11 근처"))
                .andExpect(jsonPath("$.initConsonant").value("ㅅㅇㅈㅇ"));
    }

    @Test
    @DisplayName("[/api/v1/quiz] 퀴즈가 존재하지 않는 경우")
    void quizNotExistTest() throws Exception {
        // given
        String url = "/api/v1/quiz?eventId=2&problemNumber=2";

        // when
        ResultActions result = mockMvc.perform(get(url));

        result.andExpect(status().isNoContent());
    }
}
