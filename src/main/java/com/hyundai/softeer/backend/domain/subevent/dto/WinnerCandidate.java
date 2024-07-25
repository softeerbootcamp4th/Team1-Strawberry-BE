package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WinnerCandidate {
    private Long eventUserId;
    private double randomValue;
}
