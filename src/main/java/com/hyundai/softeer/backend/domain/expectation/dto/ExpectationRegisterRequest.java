package com.hyundai.softeer.backend.domain.expectation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpectationRegisterRequest {
    @NotBlank
    @Max(300)
    private String comment;
}
