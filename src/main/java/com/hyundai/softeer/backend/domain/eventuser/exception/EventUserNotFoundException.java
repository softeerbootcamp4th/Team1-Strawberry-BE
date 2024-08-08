package com.hyundai.softeer.backend.domain.eventuser.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EventUserNotFoundException extends BaseException {
    public EventUserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 이벤트 참가자를 찾을 수 없습니다.");
    }
}
