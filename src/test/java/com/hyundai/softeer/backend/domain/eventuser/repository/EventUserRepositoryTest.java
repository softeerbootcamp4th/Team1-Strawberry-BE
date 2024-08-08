package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE, connection = EmbeddedDatabaseConnection.H2)
@Sql(scripts = "classpath:sql/integration-h2.sql")
class EventUserRepositoryTest {
    @Autowired
    private EventUserRepository eventUserRepository;

    @Test
    @DisplayName("findByUserIdAndSubEventId success")
        //    Optional<EventUser> findByUserIdAndSubEventId(long userId, long subEventId);
    void findByUserIdAndSubEventId() {
        // given

        //when
        Optional<EventUser> result = eventUserRepository.findByUserIdAndSubEventId(1L, 1L);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByUserIdAndSubEventId notfound")
        //    Optional<EventUser> findByUserIdAndSubEventId(long userId, long subEventId);
    void findByUserIdAndSubEventId_Notfound() {
        // given

        //when
        Optional<EventUser> result = eventUserRepository.findByUserIdAndSubEventId(1L, 100L);

        //then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("findNByRand success")
        // List<EventUser> findNByRand(long subEventId, int rand, int limit);
    void findNByRand() {
        // given

        //when
        List<EventUser> result = eventUserRepository.findNByRand(1L, 1, 3);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("findNByRand wrongEventId")
        // List<EventUser> findNByRand(long subEventId, int rand, int limit);
    void findNByRand_wrongEventId() {
        // given

        //when
        List<EventUser> result = eventUserRepository.findNByRand(1000L, 1, 3);

        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findRestByRand success")
        // List<EventUser> findRestByRand(long subEventId, int limit);
    void findRestByRand() {
        // given

        //when
        List<EventUser> result = eventUserRepository.findRestByRand(1L, 3);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("countBySubEventId success")
        // long countBySubEventId(Long subEventId);
    void findMaxBySubEventId() {
        // given
        eventUserRepository.save(EventUser.builder()
                .id(1000L)
                .subEvent(SubEvent.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .build());

        //when 실제 integeration 에 존재하는 eventUser 의 최댓값
        long result = eventUserRepository.findMaxBySubEventId(1L);

        //then
        assertThat(result).isEqualTo(1000L);
    }

}