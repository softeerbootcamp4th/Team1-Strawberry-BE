package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DrawingScoreDto {
    @NotNull
    List<PositionDto> positions;

    @NotNull
    Long subEventId;

    @NotNull
    Integer sequence;
}
