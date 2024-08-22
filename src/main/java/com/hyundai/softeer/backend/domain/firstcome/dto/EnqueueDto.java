package com.hyundai.softeer.backend.domain.firstcome.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnqueueDto {
    String token;
    Long currentWaitingUsers;
}
