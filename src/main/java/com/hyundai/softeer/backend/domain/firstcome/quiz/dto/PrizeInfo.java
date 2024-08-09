package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PrizeInfo {

    @NotBlank
    private boolean isValidPrize;

    @NotBlank
    private String prizeName;

    @NotBlank
    private String prizeImgUrl;

    @NotBlank
    private Integer quizSequence;

    @NotBlank
    private LocalDate quizEventDate;
}
