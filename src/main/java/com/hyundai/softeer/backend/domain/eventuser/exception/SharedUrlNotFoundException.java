package com.hyundai.softeer.backend.domain.eventuser.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SharedUrlNotFoundException extends BaseException {
    public SharedUrlNotFoundException() {
        super(HttpStatus.NOT_FOUND, "잘못된 공유 주소입니다.");
    }
}
