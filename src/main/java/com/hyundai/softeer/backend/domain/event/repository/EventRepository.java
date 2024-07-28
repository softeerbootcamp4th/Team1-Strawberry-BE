package com.hyundai.softeer.backend.domain.event.repository;

import com.hyundai.softeer.backend.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
