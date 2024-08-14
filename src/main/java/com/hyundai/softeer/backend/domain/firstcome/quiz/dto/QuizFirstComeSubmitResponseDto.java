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
    private Boolean isParticipant;
    private String prizeImgUrl;

    public static QuizFirstComeSubmitResponseDto winner(String prizeImgUrl) {
        return new QuizFirstComeSubmitResponseDto(true, true, false, prizeImgUrl);
    }

    public static QuizFirstComeSubmitResponseDto correctBut() {
        return new QuizFirstComeSubmitResponseDto(true, false, false, null);
    }

    public static QuizFirstComeSubmitResponseDto notCorrect() {
        return new QuizFirstComeSubmitResponseDto(false, false, false, null);
    }

    public static QuizFirstComeSubmitResponseDto alreadyParticipant() {
        return new QuizFirstComeSubmitResponseDto(false, false, true, null);
    }
}
