package com.hyundai.softeer.backend.domain.eventuser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SharedUrlDto {
    @Schema(example = "G4i1Bc")
    private String sharedUrl;

    @Schema(example = "98.3")
    private Double gameScore;
}
