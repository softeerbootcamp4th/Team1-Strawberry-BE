package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DrawingGameInfoDto {
    @Schema(example = "https://example.s3.ap-northeast-2.amazonaws.com/drawing/contour_1.svg")
    private String contourImgUrl;

    @Schema(example = "https://example.s3.ap-northeast-2.amazonaws.com/drawing/image_1.jpg")
    private String imgUrl;

    @Schema(example = "1")
    private Integer sequence;

    public static DrawingGameInfoDto fromEntity(DrawingLotteryEvent drawingLotteryEvent) {
        return DrawingGameInfoDto.builder()
                .contourImgUrl(drawingLotteryEvent.getContourImgUrl())
                .imgUrl(drawingLotteryEvent.getImgUrl())
                .sequence(drawingLotteryEvent.getSequence())
                .build();
    }
}
