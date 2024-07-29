package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizResponseDto {

    @Schema(name = "퀴즈 문제", example = "대한민국의 수도는?")
    private String problem;

    @Schema(name = "퀴즈 정답 초성", example = "ㅅㅇ")
    private String initConsonant;

    @Schema(name = "퀴즈 정답 초성", example = "우리가 현재 있는 곳!")
    private String hint;
}
