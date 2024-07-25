package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LotteryScoreWeight {
    private double sharedWeight;
    private double priorityWeight;
    private double lottoWeight;
    private double gameWeight;
}
