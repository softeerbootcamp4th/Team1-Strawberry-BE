package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetQuizRequest {

    @NotBlank
    private Long eventId;

    @NotBlank
    private Integer problemNumber;
}
