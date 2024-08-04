package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.advice;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.exception.QuizNotFoundException;
import com.hyundai.softeer.backend.global.dto.BaseResponse;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class QuizControllerAdvice {

    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<Null> quizNotFoundException(QuizNotFoundException quizNotFoundException) {
        return BaseResponse.<Null>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(quizNotFoundException.getMessage())
                .data(null)
                .build();
    }
}
