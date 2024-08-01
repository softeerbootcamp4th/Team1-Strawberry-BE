package com.hyundai.softeer.backend.domain.lottery.drawing.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DrawingNotFoundException extends BaseException {
    public DrawingNotFoundException() {
        super(HttpStatus.NOT_FOUND, "드로잉 이벤트가 존재하지 않습니다.");
    }
}
