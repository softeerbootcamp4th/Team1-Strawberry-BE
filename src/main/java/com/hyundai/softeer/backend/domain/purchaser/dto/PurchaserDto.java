package com.hyundai.softeer.backend.domain.purchaser.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaserDto {
    private Long id;
    private Long userId;
    private String carName;
}
