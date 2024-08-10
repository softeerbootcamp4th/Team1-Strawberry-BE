package com.hyundai.softeer.backend.domain.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class MainLandDto {

    private Integer remainSecond;

    private Map<String, Object> imgs;


}
