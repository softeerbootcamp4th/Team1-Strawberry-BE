package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "1")
    Long subEventId;

    @NotNull
    @Schema(example = "2")
    Integer sequence;
}
