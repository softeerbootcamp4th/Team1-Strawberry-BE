package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.advice;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto.QuizSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.exception.NoWinnerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class QuizControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoWinnerException.class)
    public QuizSubmitResponseDto handleNoContentException(QuizNotFoundException ex) {
        return new QuizSubmitResponseDto(false, null);
    }
}
