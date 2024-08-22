package com.hyundai.softeer.backend.domain.firstcome.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WaitingEnqueueBodyRequest {
    @NotNull
    @Schema(example = "1")
    Long subEventId;
}
