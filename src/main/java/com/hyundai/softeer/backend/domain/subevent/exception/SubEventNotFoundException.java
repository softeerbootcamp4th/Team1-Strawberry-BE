package com.hyundai.softeer.backend.domain.subevent.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SubEventNotFoundException extends BaseException {
    public SubEventNotFoundException() {
        super(HttpStatus.NO_CONTENT, "존재하지 않는 서브 이벤트 입니다.");
    }
}
