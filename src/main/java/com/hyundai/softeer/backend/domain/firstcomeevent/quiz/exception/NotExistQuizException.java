package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotExistQuizException extends BaseException {
    public NotExistQuizException() {
        super(HttpStatus.BAD_REQUEST, "존재하지 않는 퀴즈입니다.");
    }
}