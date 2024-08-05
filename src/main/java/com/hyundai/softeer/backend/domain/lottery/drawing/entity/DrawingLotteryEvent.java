package com.hyundai.softeer.backend.domain.lottery.drawing.entity;

import com.hyundai.softeer.backend.domain.lottery.entity.LotteryEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "drawing_lottery_events")
@SuperBuilder
@Getter
@NoArgsConstructor
public class DrawingLotteryEvent extends LotteryEvent {
    private String drawPointsJsonUrl;

    private String contourImgUrl;

    private String imgUrl;
}