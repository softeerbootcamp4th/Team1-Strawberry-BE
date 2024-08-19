package com.hyundai.softeer.backend.domain.firstcome.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class QuizAlreadyExistException extends BaseException {
    public QuizAlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "퀴즈가 이미 존재해요.");
    }
}
