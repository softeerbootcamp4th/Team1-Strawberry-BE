package com.hyundai.softeer.backend.domain.eventuser.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NoChanceUserException extends BaseException {
    public NoChanceUserException() {
        super(HttpStatus.BAD_REQUEST, "참여 기회가 없는 사용자입니다.");
    }
}
