package com.hyundai.softeer.backend.domain.subevent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubEventType {
    DRAWING(1, "DRAWING"),
    QUIZ(1, "QUIZ");

    private int code;
    private String status;

    public static SubEventType of(int code) {
        for (SubEventType subEventHoldType : SubEventType.values()) {
            if (subEventHoldType.getCode() == code) {
                return subEventHoldType;
            }
        }
        // TODO: Exception 추가
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
