package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class GetQuizResponseDto {
    private String bannerImg;
    private String eventImg;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime serverTime;
    private String problem;
    private String hint;
    private String anchor;
    private String prizes;
}
