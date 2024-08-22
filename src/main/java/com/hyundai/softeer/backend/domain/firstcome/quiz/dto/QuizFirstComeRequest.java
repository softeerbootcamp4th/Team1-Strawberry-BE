package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class QuizFirstComeRequest {

    @Parameter
    @NotNull
    private Long subEventId;

    private String token;
}
