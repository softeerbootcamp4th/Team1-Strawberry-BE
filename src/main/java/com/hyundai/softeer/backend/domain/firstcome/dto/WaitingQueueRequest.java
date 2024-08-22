package com.hyundai.softeer.backend.domain.firstcome.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WaitingQueueRequest {
    @NotNull
    @Schema(example = "1")
    private Long subEventId;

    @NotNull
    @Schema(example = "MToxOmMwMzA0ODhjLTA0ZGYtNDcwMS1hOGU5LTA1YmUzNmJlMmM2MA")
    private String token;
}
