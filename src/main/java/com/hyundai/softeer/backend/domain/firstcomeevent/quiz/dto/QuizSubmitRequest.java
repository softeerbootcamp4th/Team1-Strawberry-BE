package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {
    @NotBlank
    private String answer;

    @NotBlank
    private Long subEventId;
}
