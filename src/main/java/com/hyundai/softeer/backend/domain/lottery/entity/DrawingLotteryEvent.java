package com.hyundai.softeer.backend.domain.lottery.entity;

import com.hyundai.softeer.backend.domain.subevent.entity.BaseSubEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class DrawingLotteryEvent extends BaseSubEvent {
    

    private String drawPointsJsonUrl;

    private String contourImageUrl;

}