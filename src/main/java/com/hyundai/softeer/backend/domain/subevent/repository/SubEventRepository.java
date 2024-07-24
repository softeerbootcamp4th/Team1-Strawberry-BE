package com.hyundai.softeer.backend.domain.subevent.repository;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubEventRepository extends JpaRepository<SubEvent, Long> {
}
