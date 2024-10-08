package com.hyundai.softeer.backend.domain.lottery.drawing.entity;

import com.hyundai.softeer.backend.domain.lottery.entity.LotteryEvent;
import jakarta.persistence.Column;
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

    private String blurImgUrl;

    private Integer sequence;

    private String alias;

    private String onBoardingMsg;

    private String playMsg;

    private String resultDetail;

    @Column(name = "start_pos_x")
    private Double startPosX;

    @Column(name = "start_pos_y")
    private Double startPosY;
}
