package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DrawingScoreDto {
    private double score;
}
