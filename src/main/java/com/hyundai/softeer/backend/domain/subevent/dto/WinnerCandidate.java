package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WinnerCandidate {
    private Long eventUserId;
    private double randomValue;

    @Override
    public String toString() {
        return "WinnerCandidate{" +
                "eventUserId=" + eventUserId +
                ", randomValue=" + randomValue +
                '}';
    }
}
