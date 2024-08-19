package com.hyundai.softeer.backend.domain.user.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AlreadyExistOtherOAuthProviderException extends BaseException {
    public AlreadyExistOtherOAuthProviderException() {
        super(HttpStatus.CONFLICT, "이미 다른 소셜 계정으로 가입된 이메일 혹은 전화번호입니다.");
    }
}
