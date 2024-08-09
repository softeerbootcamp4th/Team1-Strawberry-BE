package com.hyundai.softeer.backend.domain.lottery.drawing.repository;

import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE, connection = EmbeddedDatabaseConnection.H2)
@Sql(scripts = "classpath:sql/integration-h2.sql")
class DrawingLotteryRepositoryTest {
    @Autowired
    private DrawingLotteryRepository drawingLotteryRepository;

    @Test
    @DisplayName("findBySubEventId success")
        // List<DrawingLotteryEvent> findBySubEventId(Long subEventId);
    void findBySubEventId() {
        // given

        //when
        List<DrawingLotteryEvent> result = drawingLotteryRepository.findBySubEventId(4L);

        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getAlias()).isEqualTo("1차 드로잉");
    }
}