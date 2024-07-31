package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class QuizNotFoundException extends BaseException {
    public QuizNotFoundException() {
        super(HttpStatus.NO_CONTENT, "존재하지 않습니다.");
    }
}
