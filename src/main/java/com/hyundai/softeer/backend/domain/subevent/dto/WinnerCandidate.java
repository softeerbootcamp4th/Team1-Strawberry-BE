package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class WinnerCandidate {
    private long eventUserId;
    private double randomValue;
    private long userId;
}
