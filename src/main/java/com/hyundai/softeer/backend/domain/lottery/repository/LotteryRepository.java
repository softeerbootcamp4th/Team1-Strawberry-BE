package com.hyundai.softeer.backend.domain.lottery.repository;

import com.hyundai.softeer.backend.domain.lottery.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryRepository extends JpaRepository<Lottery, Long> {
}
