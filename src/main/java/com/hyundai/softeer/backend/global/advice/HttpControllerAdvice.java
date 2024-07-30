package com.hyundai.softeer.backend.global.advice;

import com.hyundai.softeer.backend.global.exception.JsonParseException;
import com.hyundai.softeer.backend.global.exception.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HttpControllerAdvice {
    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> handleNoContentException(NoContentException ex) {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Void> jsonParseException(NoContentException ex) {
        return ResponseEntity.internalServerError().build();
    }
}
