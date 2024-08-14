package com.hyundai.softeer.backend.global.exception.error;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class JwtException extends BaseException {
    public JwtException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
