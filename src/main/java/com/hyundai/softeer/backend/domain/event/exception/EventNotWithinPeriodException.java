package com.hyundai.softeer.backend.domain.event.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EventNotWithinPeriodException extends BaseException {
    public EventNotWithinPeriodException() {
        super(HttpStatus.ACCEPTED, "이벤트 기간이 아니에요.");
    }
}
