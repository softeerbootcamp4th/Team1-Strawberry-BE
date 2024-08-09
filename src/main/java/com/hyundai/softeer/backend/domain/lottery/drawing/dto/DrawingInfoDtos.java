package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DrawingInfoDtos {
    List<DrawingGameInfoDto> gameInfos;
}
