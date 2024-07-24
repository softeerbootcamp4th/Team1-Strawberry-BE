package com.hyundai.softeer.backend.domain.lottery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "drawing_lotteries")
@SuperBuilder
@Getter
@NoArgsConstructor
public class DrawingLottery extends Lottery {
    private String drawPointsJsonUrl;
    private String contourImageUrl;
    private String imageUrl;
}
