package com.hyundai.softeer.backend.domain.lottery.entity;

import com.hyundai.softeer.backend.domain.subevent.entity.BaseSubEvent;
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
public class DrawingLottery extends BaseSubEvent {
    private String drawPointsJsonUrl;

    private String contourImageUrl;

    private String imageUrl;
}
