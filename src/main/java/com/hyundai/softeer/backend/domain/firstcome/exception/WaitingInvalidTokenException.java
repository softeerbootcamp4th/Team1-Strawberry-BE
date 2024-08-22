package com.hyundai.softeer.backend.domain.firstcome.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WaitingInvalidTokenException extends BaseException {
    public WaitingInvalidTokenException() {
        super(HttpStatus.BAD_REQUEST, "대기열을 거치지 않은 접근입니다.");
    }
}
