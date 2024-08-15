package com.hyundai.softeer.backend.domain.firstcome.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class QuizRegisterForbiddenException extends BaseException {
    public QuizRegisterForbiddenException() {
        super(HttpStatus.BAD_REQUEST, "등록하고자하는 퀴즈 이벤트는 반드시 3개여야 합니다.");
    }
}
