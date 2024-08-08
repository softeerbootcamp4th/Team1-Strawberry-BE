package com.hyundai.softeer.backend.domain.subevent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubEventType {
    DRAWING(0, "DRAWING", "api/v1/lottery/drawing/land"),
    QUIZ(1, "QUIZ", "api/v1/firstcome/quiz/land"),
    ;

    private int code;
    private String status;
    private String redirectUrl;

    public static SubEventType of(int code) {
        for (SubEventType subEventHoldType : SubEventType.values()) {
            if (subEventHoldType.getCode() == code) {
                return subEventHoldType;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
