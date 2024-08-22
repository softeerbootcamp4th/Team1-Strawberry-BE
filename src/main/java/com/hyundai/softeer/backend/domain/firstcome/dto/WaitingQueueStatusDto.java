package com.hyundai.softeer.backend.domain.firstcome.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WaitingQueueStatusDto {
    private Long waitingUsers;
}
