package com.hyundai.softeer.backend.domain.subevent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventPlayType {
    NORMAL(0, "normal"),
    EXPECTATION(1, "expectation"),
    SHARED(2, "shared_url");

    private int code;
    private String status;

    public static EventPlayType of(int code) {
        for (EventPlayType eventPlayType : EventPlayType.values()) {
            if (eventPlayType.getCode() == code) {
                return eventPlayType;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}