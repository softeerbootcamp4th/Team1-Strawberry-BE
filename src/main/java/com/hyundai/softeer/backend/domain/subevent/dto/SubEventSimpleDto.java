package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class SubEventSimpleDto {
    private Long id;
    private String alias;
    private String SubEventExecuteType;
    private String SubEventType;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Map<String, Object> winnersMeta;
}
