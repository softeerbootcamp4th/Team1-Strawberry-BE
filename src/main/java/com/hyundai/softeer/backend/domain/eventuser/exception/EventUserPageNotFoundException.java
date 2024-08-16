package com.hyundai.softeer.backend.domain.eventuser.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EventUserPageNotFoundException extends BaseException {
    public EventUserPageNotFoundException() {
        super(HttpStatus.NOT_FOUND, "최대 페이지 수가 넘었습니다.");
    }
}
