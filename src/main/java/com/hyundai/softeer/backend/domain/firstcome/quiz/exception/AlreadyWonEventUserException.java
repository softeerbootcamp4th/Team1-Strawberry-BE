package com.hyundai.softeer.backend.domain.firstcome.quiz.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AlreadyWonEventUserException extends BaseException {
    public AlreadyWonEventUserException() {
        super(HttpStatus.BAD_REQUEST, "이미 이벤트에 당첨되었습니다.");
    }
}
