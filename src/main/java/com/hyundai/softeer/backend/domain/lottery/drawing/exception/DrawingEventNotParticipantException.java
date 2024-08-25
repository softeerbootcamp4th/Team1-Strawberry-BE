package com.hyundai.softeer.backend.domain.lottery.drawing.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DrawingEventNotParticipantException extends BaseException {
    public DrawingEventNotParticipantException() {
        super(HttpStatus.BAD_REQUEST, "아직 드로잉 이벤트를 참여하지 않은 유저 입니다.");
    }
}
