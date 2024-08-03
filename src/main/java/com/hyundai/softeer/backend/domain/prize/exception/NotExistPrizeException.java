package com.hyundai.softeer.backend.domain.prize.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotExistPrizeException extends BaseException {
    public NotExistPrizeException() {
        super(HttpStatus.NO_CONTENT, "존재하지 않는 상품입니다.");
    }
}
