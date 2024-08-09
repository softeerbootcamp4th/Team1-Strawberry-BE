package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PositionDto {
    @Schema(example = "10.5")
    private double x;

    @Schema(example = "5.0")
    private double y;
}
