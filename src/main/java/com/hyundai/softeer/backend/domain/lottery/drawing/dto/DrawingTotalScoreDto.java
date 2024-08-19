package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DrawingTotalScoreDto {
    @Schema(example = "80.5")
    private double totalScore;

    @Schema(example = "88.5")
    private double maxScore;

    @Schema(example = "2")
    private int chance;

    @Schema(example = "1")
    private int expectationBonusChance;

    @Schema(example = "-1")
    private int shareBonusChance;
}
