package com.hyundai.softeer.backend.global.exception.error;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SQLRuntimeException extends BaseException {
    public SQLRuntimeException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다.");
    }
}
