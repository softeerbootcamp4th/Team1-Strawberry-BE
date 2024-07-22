package com.hyundai.softeer.backend.domain.user.excepiton;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. : %s".formatted(id));
    }

    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. : %s".formatted(email));
    }
}
