package com.hyundai.softeer.backend.domain.purchaser.repository;

import com.hyundai.softeer.backend.domain.purchaser.dto.EventPurchaserCount;
import com.hyundai.softeer.backend.domain.purchaser.entity.Purchaser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaserRepository extends JpaRepository<Purchaser, Long> {
    @Query("SELECT p.event.id as eventId, COUNT(p) as purchaserCount " +
            "FROM Purchaser p " +
            "GROUP BY p.event.id")
    List<EventPurchaserCount> countPurchasersGroupedByEventId();

}
