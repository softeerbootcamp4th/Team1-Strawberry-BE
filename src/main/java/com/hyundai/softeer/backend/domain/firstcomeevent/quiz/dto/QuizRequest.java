package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequest {
    @NotBlank
    private long subEventId;
}
