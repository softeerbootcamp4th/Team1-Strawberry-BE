package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizRequest {
    @NotBlank
    private long subEventId;
}
