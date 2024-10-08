package com.hyundai.softeer.backend.domain.event.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotExistEventException extends BaseException {
    public NotExistEventException() {
        super(HttpStatus.NO_CONTENT, "존재하지 않는 이벤트 입니다.");
    }
}
