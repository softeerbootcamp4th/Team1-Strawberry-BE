package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventUserRepository extends JpaRepository<EventUser, Long> {
    EventUser findByUserIdAndSubEventId(long userId, long subEventId);

    @Query("SELECT new com.hyundai.softeer.backend.domain.lottery.dto.RankDto(u.name, eu.gameScore) FROM EventUser eu JOIN eu.user u WHERE eu.subEvent.id = :subEventId " +
            "ORDER BY eu.gameScore DESC")
    List<RankDto> findTopNBySubEventId(long subEventId, Pageable pageable);

    @Query("SELECT eu FROM EventUser eu " +
            "WHERE eu.subEvent.id = :subEventId AND eu.id >= :rand")
    List<EventUser> findNByRand(long subEventId, int rand, Pageable pageable);

    @Query("SELECT eu FROM EventUser eu " +
            "WHERE eu.subEvent.id = :subEventId")
    List<EventUser> findRestByRand(long subEventId, Pageable pageable);
}