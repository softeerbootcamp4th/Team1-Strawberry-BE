package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import com.hyundai.softeer.backend.domain.subevent.dto.SubEventInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizFirstComeInfoResponseDto {
    private SubEventInfo subEventInfo;
    private QuizInfoDto quizInfo;
}
