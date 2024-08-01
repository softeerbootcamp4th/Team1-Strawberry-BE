package com.hyundai.softeer.backend.global.status;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS(2000, "success"),
    NOCONTENT(2040, "no content");


    private final int status;
    private final String message;

    BaseResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
