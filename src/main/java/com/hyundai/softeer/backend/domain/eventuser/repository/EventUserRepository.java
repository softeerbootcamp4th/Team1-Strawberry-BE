package com.hyundai.softeer.backend.domain.eventuser.repository;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventUserRepository extends JpaRepository<EventUser, Long> {
}