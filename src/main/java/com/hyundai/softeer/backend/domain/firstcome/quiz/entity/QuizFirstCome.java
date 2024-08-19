package com.hyundai.softeer.backend.domain.firstcome.quiz.entity;

import com.hyundai.softeer.backend.domain.firstcome.entity.FirstComeEvent;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "quiz_firstcome_events")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class QuizFirstCome extends FirstComeEvent {

    private Integer sequence;

    private String overview;

    private String problem;

    private String carInfo;

    private String answer;

    private String hint;
    /**
     * 추후 결정
     */
    private String anchor;

    private String initConsonant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prize prize;

    @Builder.Default
    private Integer winnerCount = 0;
}
