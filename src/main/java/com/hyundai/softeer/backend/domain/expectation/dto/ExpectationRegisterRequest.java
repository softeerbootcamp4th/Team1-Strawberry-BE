package com.hyundai.softeer.backend.domain.expectation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
public class ExpectationRegisterRequest {

    @NotBlank
    @Size(max=300)
    private String comment;
}
