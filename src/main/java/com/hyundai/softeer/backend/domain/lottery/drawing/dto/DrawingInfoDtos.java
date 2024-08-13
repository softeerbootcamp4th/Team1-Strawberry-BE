package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class DrawingInfoDtos {
    private List<DrawingGameInfoDto> gameInfos;

    @Schema(example = "1")
    private int chance;
}
