package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizInfoDto {
    private Integer sequence;

    private String overview;

    private String problem;

    private String carInfo;

    private String answer;

    private String hint;

    private String anchor;

    private String initConsonant;

    private Integer winnerCount;

    public static QuizInfoDto fromEntity(QuizFirstCome quiz) {
        return QuizInfoDto.builder()
                .sequence(quiz.getSequence())
                .overview(quiz.getOverview())
                .problem(quiz.getProblem())
                .carInfo(quiz.getCarInfo())
                .answer(quiz.getAnswer())
                .hint(quiz.getHint())
                .anchor(quiz.getAnchor())
                .initConsonant(quiz.getInitConsonant())
                .winnerCount(quiz.getWinnerCount())
                .build();
    }
}
