package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizFirstComeInfoRequest {

    @NotNull
    private Long eventId;
<<<<<<< HEAD
}
=======
}
>>>>>>> dd32723 (fix: QuizFirstComeInfoRequest가 없는 문제 해결)
