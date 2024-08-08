package com.hyundai.softeer.backend.domain.subevent.repository;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Sql(value = {"/sql/integration.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class SubEventRepositoryTest {

    @Autowired
    SubEventRepository subEventRepository;

    @Test
    void test() {
        // given
        long eventId = 1L;
        SubEventExecuteType executeType = SubEventExecuteType.FIRSTCOME;

        // when
        List<SubEvent> subEvents = subEventRepository.findByEventIdAndExecuteType(eventId, executeType);

        // then
        assertThat(subEvents).isNotEmpty();
        assertThat(subEvents.get(0).getAlias()).isEqualTo("퀴즈 테스트");
    }

}