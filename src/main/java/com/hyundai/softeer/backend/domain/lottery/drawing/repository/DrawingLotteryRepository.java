package com.hyundai.softeer.backend.domain.lottery.drawing.repository;

import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawingLotteryRepository extends JpaRepository<DrawingLotteryEvent, Long> {
    List<DrawingLotteryEvent> findBySubEventId(Long subEventId);
}
