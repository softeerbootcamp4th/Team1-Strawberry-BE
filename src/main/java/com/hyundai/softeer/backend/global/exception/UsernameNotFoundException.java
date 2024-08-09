package com.hyundai.softeer.backend.global.exception;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends BaseException {
    public UsernameNotFoundException(String email) {
        super(HttpStatus.UNAUTHORIZED, email + " not found");
    }
}
