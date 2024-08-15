package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.projection.EventUserProjection;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventUserRepository extends JpaRepository<EventUser, Long> {
    Optional<EventUser> findByUserIdAndSubEventId(long userId, long subEventId);

    @Query("SELECT new com.hyundai.softeer.backend.domain.lottery.dto.RankDto(u.name, eu.gameScore) FROM EventUser eu JOIN eu.user u WHERE eu.subEvent.id = :subEventId " +
            "ORDER BY eu.gameScore DESC")
    List<RankDto> findTopNBySubEventId(long subEventId, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM event_users as eu " +
            "WHERE eu.sub_event_id = :subEventId AND eu.id >= :rand " +
            "LIMIT :limit", nativeQuery = true)
    List<EventUser> findNByRand(long subEventId, int rand, int limit);

    @Query(value = "SELECT * " +
            "FROM event_users as eu " +
            "WHERE eu.sub_event_id = :subEventId " +
            "LIMIT :limit", nativeQuery = true)
    List<EventUser> findRestByRand(long subEventId, int limit);

    @Query("SELECT MAX(eu.id) FROM EventUser eu WHERE eu.subEvent.id = :subEventId")
    long findMaxBySubEventId(Long subEventId);

    @Query(value = "SELECT u.name as name, u.birth_date as birthDate, u.phone_number as phoneNumber, u.email as email, " +
            "CASE WHEN SUM(CASE WHEN eu.is_winner THEN 1 ELSE 0 END) > 0 THEN 1 ELSE 0 END as isWinner "+
            "FROM users u " +
            "JOIN event_users eu ON u.id = eu.user_id " +
            "WHERE eu.event_id = :eventId " +
            "GROUP BY u.id, u.name, u.birth_date, u.phone_number, u.email " +
            "ORDER BY u.id " +
            "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<EventUserProjection> findEventUserWithWinnerState(@Param("eventId") Long eventId,
                                                           @Param("offset") int offset,
                                                           @Param("limit") int limit);

    @Query(value = "SELECT COUNT(DISTINCT eu.user_id) " +
            "FROM event_users eu " +
            "WHERE eu.event_id = :eventId",
            nativeQuery = true)
    Integer countEventUserSize(@Param("eventId") Long eventId);

    Optional<EventUser> findBySharedUrl(String sharedUrl);

    List<EventUser> findByEventId(Long eventId, Pageable pageable);
}