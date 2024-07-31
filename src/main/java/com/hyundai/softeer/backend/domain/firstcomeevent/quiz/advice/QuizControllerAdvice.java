package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.advice;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitRequest;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitResponseDto;
import com.hyundai.softeer.backend.domain.subevent.exception.NoWinnerException;
import com.hyundai.softeer.backend.global.exception.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class QuizControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoWinnerException.class)
    public QuizSubmitResponseDto handleNoContentException(NoContentException ex) {
        return new QuizSubmitResponseDto(false, null);
    }
}
