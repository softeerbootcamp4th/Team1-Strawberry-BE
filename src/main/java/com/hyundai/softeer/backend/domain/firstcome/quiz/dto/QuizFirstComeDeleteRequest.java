package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizFirstComeDeleteRequest {
    @NotNull
    private Long eventId;
}
