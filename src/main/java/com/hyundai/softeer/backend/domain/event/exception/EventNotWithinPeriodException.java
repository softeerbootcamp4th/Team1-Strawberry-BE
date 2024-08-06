package com.hyundai.softeer.backend.domain.event.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EventNotWithinPeriodException extends BaseException {
    public EventNotWithinPeriodException() {
        super(HttpStatus.NOT_FOUND, "이벤트 기간이 아닙니다.");
    }
}
