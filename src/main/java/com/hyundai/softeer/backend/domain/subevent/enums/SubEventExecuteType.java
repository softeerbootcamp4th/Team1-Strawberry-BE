package com.hyundai.softeer.backend.domain.subevent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubEventExecuteType {
    LOTTERY(1, "LOTTERY"),
    FIRSTCOME(2, "FIRSTCOME");

    private int code;
    private String status;

    public static SubEventExecuteType of(int code) {
        for (SubEventExecuteType subEventExecuteType : SubEventExecuteType.values()) {
            if (subEventExecuteType.getCode() == code) {
                return subEventExecuteType;
            }
        }
        // TODO: Exception 추가
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
