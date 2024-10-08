package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class WinnerInfo {
    private int winnerCount;
    private long prizeId;
    private int rank;
}
