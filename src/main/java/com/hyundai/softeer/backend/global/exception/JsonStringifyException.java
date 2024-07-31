package com.hyundai.softeer.backend.global.exception;

public class JsonStringifyException extends RuntimeException {
    public JsonStringifyException() {
        super();
    }

    public JsonStringifyException(String message) {
        super(message);
    }

    public JsonStringifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonStringifyException(Throwable cause) {
        super(cause);
    }
}
