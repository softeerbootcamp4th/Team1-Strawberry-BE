package com.hyundai.softeer.backend.domain.firstcome.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class JsonParseException extends BaseException {
    public JsonParseException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다.");
    }
}
