package com.hyundai.softeer.backend.domain.lottery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Drawing_lotteries")
@SuperBuilder
@Getter
@NoArgsConstructor
public class DrawingLottery extends Lottery {
    private String drawPointsJsonUrl;

    private String contourImageUrl;

    private String imageUrl;
}
