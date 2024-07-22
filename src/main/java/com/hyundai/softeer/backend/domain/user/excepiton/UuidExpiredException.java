package com.hyundai.softeer.backend.domain.user.excepiton;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UuidExpiredException extends BaseException {
    public UuidExpiredException() {
        super(HttpStatus.BAD_REQUEST, "인증에 실패했습니다.");
    }
}
