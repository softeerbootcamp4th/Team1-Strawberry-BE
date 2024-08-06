package com.hyundai.softeer.backend.global.exception.error;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class JsonParseException extends BaseException {
    public JsonParseException() {
        super(HttpStatus.BAD_REQUEST, "Json Parse 에러가 발생했습니다.");
    }
}
