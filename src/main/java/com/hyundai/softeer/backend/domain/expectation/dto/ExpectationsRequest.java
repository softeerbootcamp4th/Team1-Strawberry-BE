package com.hyundai.softeer.backend.domain.expectation.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class ExpectationsRequest {

    @NotNull
    @Parameter
    private Integer pageSequence;
}
