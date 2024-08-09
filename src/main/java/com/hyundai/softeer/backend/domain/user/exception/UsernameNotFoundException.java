package com.hyundai.softeer.backend.domain.user.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends BaseException {
    public UsernameNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다.");
    }
}
