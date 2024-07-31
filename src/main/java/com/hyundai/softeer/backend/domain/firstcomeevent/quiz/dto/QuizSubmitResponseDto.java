package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuizSubmitResponseDto {
   private Boolean isCorrect;
   private String prizeImgUrl;
}
