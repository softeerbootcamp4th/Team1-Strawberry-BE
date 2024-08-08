package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Data
@NoArgsConstructor
public class QuizSubmitRequest {

    @NotBlank
    private String answer;

    @NotNull
    private Long subEventId;
}
