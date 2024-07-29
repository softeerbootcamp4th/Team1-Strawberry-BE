package com.hyundai.softeer.backend.domain.subevent.repository;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubEventRepository extends JpaRepository<SubEvent, Long> {

    List<SubEvent> findByEventId(Long eventId);
}
