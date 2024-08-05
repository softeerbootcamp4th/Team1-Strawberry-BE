package com.hyundai.softeer.backend.domain.subevent.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SubEventNotWithinPeriodException extends BaseException {
    public SubEventNotWithinPeriodException() {
        super(HttpStatus.ACCEPTED, "이벤트 기간이 아닙니다.");
    }
}
