package com.hyundai.softeer.backend.domain.winner.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WinnerInfoDto {
    private Long userId;
    private int ranking;
    private String prizeName;
}
