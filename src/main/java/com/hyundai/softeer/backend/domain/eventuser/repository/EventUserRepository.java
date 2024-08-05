package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventUserRepository extends JpaRepository<EventUser, Long> {
    EventUser findByUserIdAndSubEventId(Long userId, Long subEventId);

    @Query("SELECT new com.hyundai.softeer.backend.domain.lottery.dto.RankDto(u.name, eu.gameScore) FROM EventUser eu JOIN eu.user u WHERE eu.subEvent.id = :subEventId " +
            "ORDER BY eu.gameScore DESC")
    List<RankDto> findTopNBySubEventId(Long subEventId, Pageable pageable);
}