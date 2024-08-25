package com.hyundai.softeer.backend.domain.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateEventPeriodRequest {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
