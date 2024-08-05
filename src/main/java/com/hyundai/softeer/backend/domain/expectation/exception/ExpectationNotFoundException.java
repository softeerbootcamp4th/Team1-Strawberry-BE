package com.hyundai.softeer.backend.domain.expectation.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ExpectationNotFoundException extends BaseException {
    public ExpectationNotFoundException() {
        super(HttpStatus.NOT_FOUND, "기대평이 존재하지 않습니다.");
    }
}
