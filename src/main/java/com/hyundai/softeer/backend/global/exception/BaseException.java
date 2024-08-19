package com.hyundai.softeer.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Boolean isCustom;

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.isCustom = true;
    }

}
