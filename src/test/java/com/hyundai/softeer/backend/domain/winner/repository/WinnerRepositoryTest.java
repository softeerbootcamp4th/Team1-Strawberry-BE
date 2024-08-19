package com.hyundai.softeer.backend.domain.winner.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE, connection = EmbeddedDatabaseConnection.H2)
@Sql(scripts = "classpath:sql/integration-h2.sql")
class WinnerRepositoryTest {

    @Autowired
    private WinnerRepository winnerRepository;

    @Test
    @DisplayName("서브이벤트 당첨자 수 조회 쿼리")
    void countWinnerBySubEventIdTest() {
        // given
        Long subEventId = 1L;

        // when
        Long winners = winnerRepository.countWinnerBySubEventId(subEventId);

        // then
        Assertions.assertThat(winners).isEqualTo(1L);
    }

}