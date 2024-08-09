package com.hyundai.softeer.backend.domain.user.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateUserException extends BaseException {
    public DuplicateUserException() {
        super(HttpStatus.CONFLICT, "이미 존재하는 이메일 혹은 전화번호입니다.");
    }
}
