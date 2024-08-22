package com.hyundai.softeer.backend.domain.firstcome.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WaitingQueueRequest {
    private Long subEventId;
    private String token;
}
