package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateSubEventPeriodRequest {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
