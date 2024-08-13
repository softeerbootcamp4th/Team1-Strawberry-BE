package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrawingScoreRequest {
    @NotNull
    List<PositionDto> positions;

    @NotNull
    Long subEventId;

    @NotNull
    Integer sequence;
}
