package com.hyundai.softeer.backend.domain.event.repository;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e.id AS eventId, COUNT(DISTINCT eu.user_id) AS totalUsers " +
            "FROM Events e " +
            "JOIN Sub_events se ON e.id = se.event_id " +
            "JOIN event_users eu ON se.id = eu.sub_event_id " +
            "GROUP BY e.id",
            nativeQuery = true)
    List<Object[]> findTotalUsersByEventNative();
}
