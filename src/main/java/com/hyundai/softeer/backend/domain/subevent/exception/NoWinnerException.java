package com.hyundai.softeer.backend.domain.subevent.exception;

public class NoWinnerException extends RuntimeException {
    public NoWinnerException() {
        super();
    }

    public NoWinnerException(String message) {
        super(message);
    }

    public NoWinnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoWinnerException(Throwable cause) {
        super(cause);
    }
}
