package com.hyundai.softeer.backend.domain.user.excepiton;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class LoginFailException extends BaseException {

    public LoginFailException() {
        super(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다.");
    }
}
