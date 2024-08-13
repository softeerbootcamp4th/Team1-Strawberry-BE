package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DrawingScoreDto {
    @Schema(example = "80.5")
    private double score;
}
