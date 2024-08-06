package com.hyundai.softeer.backend.domain.expectation.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@AllArgsConstructor
@ParameterObject
public class ExpectationsRequest {
    @NotBlank
    @Parameter
    private int pageSequence;

    @NotBlank
    @Parameter
    private Long eventId;
}
