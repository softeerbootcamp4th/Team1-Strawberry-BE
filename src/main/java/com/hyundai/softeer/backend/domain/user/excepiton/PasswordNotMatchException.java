package com.hyundai.softeer.backend.domain.user.excepiton;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException() {
        super(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
    }
}
