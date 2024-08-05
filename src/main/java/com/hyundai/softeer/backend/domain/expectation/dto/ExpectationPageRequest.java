package com.hyundai.softeer.backend.domain.expectation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpectationPageRequest {
    @NotBlank
    private long eventId;
}
