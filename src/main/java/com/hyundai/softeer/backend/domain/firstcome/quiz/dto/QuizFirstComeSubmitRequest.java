package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuizFirstComeSubmitRequest {

    @NotBlank
    private String answer;

    @NotNull
    private Long subEventId;
}
