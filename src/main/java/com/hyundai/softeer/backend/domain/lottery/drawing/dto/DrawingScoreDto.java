package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DrawingScoreDto {
    @Schema(example = "80.5")
    private double score;

    @Schema(example = "https://www.example.com/blur.jpg")
    private String blurImgUrl;

    @Schema(example = "조금만 더 하면 고득점!")
    private String resultMsg;

    @Schema(example = "디 올 뉴 싼타페의 실내는 언제 어디서든 아웃도어 라이프를 즐길 수 있는 넉넉한 거주 공간을 자랑하며\\n 수평과 수직 이미지를 강조한 레이아웃으로 외장과 자연스럽게 어우러질 수 있도록 디자인했습니다.")
    private String resultDetail;
}
