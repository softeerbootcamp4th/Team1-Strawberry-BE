package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubEventSimpleDto {
    private Long id;
    private String alias;
    private String SubEventExecuteType;
    private String SubEventType;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
