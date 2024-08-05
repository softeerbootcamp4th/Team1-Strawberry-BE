package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class QuizSubmitResponseDto {
   private Boolean isCorrect;
   private Boolean isWinner;
   private String prizeImgUrl;

   public static QuizSubmitResponseDto winner(String prizeImgUrl) {
     return new QuizSubmitResponseDto(true, true, prizeImgUrl);
   }

   public static QuizSubmitResponseDto correctBut() {
      return new QuizSubmitResponseDto(true, false, null);
   }

   public static QuizSubmitResponseDto notCorrect() {
      return new QuizSubmitResponseDto(false, false, null);
   }
}
