package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.converter.ProblemConverter;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.QuizProblems;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class QuizResponseDto {

    @Schema(description = "퀴즈 이벤트 ID", example = "1")
    private Long subEventId;

    @Schema(description = "퀴즈 개요", example = "디 올 뉴 산타페의")
    private String overview;

    @Schema(description = "퀴즈 문제", example = "통합 연비는?")
    private String problem;

    @Schema(description = "퀴즈 정보", example = "디 올 뉴 산타페의 특징은 어쩌구 저쩌구")
    private String carInfo;

    @Schema(description = "퀴즈 정답 초성", example = "ㅅㅇ")
    private String initConsonant;

    @Schema(description = "퀴즈 힌트", example = "10 근처 일지도..?")
    private String hint;
}
