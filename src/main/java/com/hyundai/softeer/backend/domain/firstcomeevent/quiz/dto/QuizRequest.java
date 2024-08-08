package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizRequest {
    @NotBlank
    private Long subEventId;
}
