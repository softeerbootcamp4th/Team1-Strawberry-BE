package com.hyundai.softeer.backend.domain.eventuser.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NonPlayedUserException extends BaseException {
    public NonPlayedUserException() {
        super(HttpStatus.BAD_REQUEST, "아직 한 번도 참여하지 않은 사용자입니다.");
    }
}
