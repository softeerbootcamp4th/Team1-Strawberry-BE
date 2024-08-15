package com.hyundai.softeer.backend.domain.eventuser.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EventUserPageNotExistException extends BaseException {
    public EventUserPageNotExistException() {
        super(HttpStatus.NOT_FOUND, "이벤트 유저 페이지 정보가 존재하지 않습니다.");
    }
}
