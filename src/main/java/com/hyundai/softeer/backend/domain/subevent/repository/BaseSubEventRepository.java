package com.hyundai.softeer.backend.domain.subevent.repository;

import com.hyundai.softeer.backend.domain.subevent.entity.BaseSubEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseSubEventRepository extends JpaRepository<BaseSubEvent, Long> {
}
