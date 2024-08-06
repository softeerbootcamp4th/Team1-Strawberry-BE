package com.hyundai.softeer.backend.domain.expectation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ExpectationRegisterRequest {

    @NotBlank
    private Long eventId;

    @NotBlank
    @Max(300)
    private String comment;
}
