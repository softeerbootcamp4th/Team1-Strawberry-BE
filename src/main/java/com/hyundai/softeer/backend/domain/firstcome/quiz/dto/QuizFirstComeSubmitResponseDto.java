package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class QuizFirstComeSubmitResponseDto {
    private Boolean isCorrect;
    private Boolean isWinner;
    private String prizeImgUrl;

    public static QuizFirstComeSubmitResponseDto winner(String prizeImgUrl) {
        return new QuizFirstComeSubmitResponseDto(true, true, prizeImgUrl);
    }

    public static QuizFirstComeSubmitResponseDto correctBut() {
        return new QuizFirstComeSubmitResponseDto(true, false, null);
    }

    public static QuizFirstComeSubmitResponseDto notCorrect() {
        return new QuizFirstComeSubmitResponseDto(false, false, null);
    }
}
