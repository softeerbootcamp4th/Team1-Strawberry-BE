package com.hyundai.softeer.backend.domain.lottery.repository;

import com.hyundai.softeer.backend.domain.subevent.entity.BaseSubEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryRepository extends JpaRepository<BaseSubEvent, Long> {
}
