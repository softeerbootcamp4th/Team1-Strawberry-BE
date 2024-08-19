package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.projection.EventUserPageProjection;
import com.hyundai.softeer.backend.domain.eventuser.service.EventUserService;
import com.hyundai.softeer.backend.domain.expectation.constant.ExpectationPage;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE, connection = EmbeddedDatabaseConnection.H2)
@Sql(scripts = "classpath:sql/integration-h2.sql")
class EventUserRepositoryTest {
    @Autowired
    private EventUserRepository eventUserRepository;
    @Autowired
    private UserRepository userRepository;

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
        assertThat(result).isEqualTo(4L);
    }

    @Test
    @DisplayName("findByEmailOrPhoneNumber 이메일로 유저 검색")
        // Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
    void findByEmailOrPhoneNumber_Email() {
        // given

        //when
        List<User> result = userRepository.findByEmailOrPhoneNumber("sarang@naver.com", "010-1234-5678");

        //then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(0).getEmail()).isEqualTo("sarang@naver.com");
    }

    @Test
    @DisplayName("findByEmailOrPhoneNumber 전화번호로 유저 검색")
        // Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
    void findByEmailOrPhoneNumber_PhoneNumber() {
        // given

        //when
        List<User> result = userRepository.findByEmailOrPhoneNumber("empty@naver.com", "010-5432-4234");

        //then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(0).getEmail()).isEqualTo("sarang@naver.com");
    }

    @Test
    @DisplayName("findByEmailOrPhoneNumber 이메일, 전화번호로 유저 검색")
        // Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
    void findByEmailOrPhoneNumber_EmailAndPhoneNumber() {
        // given

        //when
        List<User> result = userRepository.findByEmailOrPhoneNumber("sarang@naver.com", "010-5432-4234");

        //then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(0).getEmail()).isEqualTo("sarang@naver.com");
    }

    @Test
    @DisplayName("findByEmailOrPhoneNumber 이메일, 전화번호로 두 유저 검색")
        // Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
    void findByEmailOrPhoneNumber_EmailAndPhoneNumber_Other() {
        // given

        //when
        List<User> result = userRepository.findByEmailOrPhoneNumber("sarang@naver.com", "010-1234-4234");

        //then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("findByEmailOrPhoneNumber Not Found")
        // Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
    void findByEmailOrPhoneNumber_NotFound() {
        // given

        //when
        List<User> result = userRepository.findByEmailOrPhoneNumber("123@naver.com", "010-0000-4234");

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findEventUsersWithWinnerStatus success test")
    void findEventUsersWithWinnerStatusSuccessTest() {
        // given
        Long subEventId = 1L;
        Pageable pageable = PageRequest.of(
                0,
                EventUserService.PAGE_SIZE
        );

        // when
        Page<EventUserPageProjection> _eventUsersWithWinnerStatus = eventUserRepository.findEventUsersWithWinnerStatus(subEventId, pageable);
        List<EventUserPageProjection> eventUsersWithWinnerStatus = _eventUsersWithWinnerStatus.get().toList();

        // then
        assertThat(_eventUsersWithWinnerStatus.getTotalPages()).isEqualTo(1);
        assertThat(eventUsersWithWinnerStatus.get(0).getUser().getId()).isEqualTo(1L);
        assertThat(eventUsersWithWinnerStatus.get(1).getUser().getId()).isEqualTo(2L);
        assertThat(eventUsersWithWinnerStatus.get(2).getUser().getId()).isEqualTo(3L);
        assertThat(eventUsersWithWinnerStatus.get(0).getIsWinner()).isFalse();
        assertThat(eventUsersWithWinnerStatus.get(1).getIsWinner()).isFalse();
        assertThat(eventUsersWithWinnerStatus.get(2).getIsWinner()).isTrue();
    }
}