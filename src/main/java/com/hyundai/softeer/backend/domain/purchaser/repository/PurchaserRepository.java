package com.hyundai.softeer.backend.domain.purchaser.repository;

import com.hyundai.softeer.backend.domain.purchaser.entity.Purchaser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaserRepository extends JpaRepository<Purchaser, Long> {
}
