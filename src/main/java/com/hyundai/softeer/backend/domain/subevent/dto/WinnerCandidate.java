package com.hyundai.softeer.backend.domain.subevent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class WinnerCandidate {
    @Schema(example = "1")
    private long eventUserId;

    @Schema(example = "99.12")
    private double randomValue;

    @Schema(example = "1")
    private long userId;
}
