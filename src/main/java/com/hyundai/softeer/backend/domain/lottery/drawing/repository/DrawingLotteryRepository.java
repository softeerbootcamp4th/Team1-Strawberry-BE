package com.hyundai.softeer.backend.domain.lottery.drawing.repository;

import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawingLotteryRepository extends JpaRepository<DrawingLotteryEvent, Long> {

    
}
