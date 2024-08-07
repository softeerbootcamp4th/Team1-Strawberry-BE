package com.hyundai.softeer.backend.domain.lottery.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AlreadyDrawedException extends BaseException {
    public AlreadyDrawedException() {
        super(HttpStatus.BAD_REQUEST, "이미 추첨이 완료된 이벤트입니다.");
    }
}
