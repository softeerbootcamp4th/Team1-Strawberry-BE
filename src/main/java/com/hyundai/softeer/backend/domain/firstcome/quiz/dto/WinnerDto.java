package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WinnerDto {
    private long userId;
    private long subEventId;
    private long prizeId;
    private int ranking;
}
