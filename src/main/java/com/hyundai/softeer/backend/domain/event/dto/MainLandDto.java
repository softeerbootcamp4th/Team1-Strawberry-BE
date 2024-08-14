package com.hyundai.softeer.backend.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class MainLandDto {

    private Integer remainSecond;

    private Map<String, Object> imgs;

    private List<QuizStartInfo> quizEventStartAt;
}
