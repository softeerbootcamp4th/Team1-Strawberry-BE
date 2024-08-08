package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    Optional<EventUser> findBySharedUrl(String sharedUrl);

}