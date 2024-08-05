package com.hyundai.softeer.backend.domain.prize.repository;

import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrizeRepository extends JpaRepository<Prize, Long> {
}
