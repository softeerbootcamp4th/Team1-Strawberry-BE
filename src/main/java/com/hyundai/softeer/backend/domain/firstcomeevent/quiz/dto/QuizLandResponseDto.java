package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class QuizLandResponseDto {

    @Schema(description = "배너 이미지 Url", example = "https://www.awss3/bannerImage")
    private String bannerImg;

    @Schema(description = "이벤트 이미지 Url", example = "https://www.awss3/eventImg")
    private String eventImg;

    @Schema(description = "시작 시간", example = "2024-07-26T10:30:00.000Z")
    private LocalDateTime startTime;

    @Schema(description = "마감 시간", example = "2024-07-27T10:30:00.000Z")
    private LocalDateTime endTime;

    @Schema(description = "서버 시간", example = "2024-07-26T18:30:00.000Z")
    private LocalDateTime serverTime;

    @Schema(description = "퀴즈 개요", example = "디 올 뉴 산타페 하이브리드의")
    private String overview;

    @Schema(description = "퀴즈 문제", example = "산타페의 연비는?")
    private String problem;

    @Schema(description = "퀴즈 힌트", example = "10.x 입니다.")
    private String hint;

    @Schema(description = "html anchor", example = "???")
    private String anchor;

    @Schema(description = "이벤트 상품 정보", example = "JSON 형식이에요 구체적인건 아직 미정")
    private String prizes;
}
