package com.hyundai.softeer.backend.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventSimpleDto {
    private Long id;
    private String eventName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String carName;
}
