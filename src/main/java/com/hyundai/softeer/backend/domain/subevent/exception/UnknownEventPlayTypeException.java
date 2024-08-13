package com.hyundai.softeer.backend.domain.subevent.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnknownEventPlayTypeException extends BaseException {
    public UnknownEventPlayTypeException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 Event Play Type 입니다.");
    }
}
