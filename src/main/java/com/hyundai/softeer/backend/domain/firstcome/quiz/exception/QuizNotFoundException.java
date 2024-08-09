package com.hyundai.softeer.backend.domain.firstcome.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class QuizNotFoundException extends BaseException {
    public QuizNotFoundException() {
        super(HttpStatus.NOT_FOUND, "퀴즈가 존재하지 않습니다.");
    }
}
