package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity;

import com.hyundai.softeer.backend.domain.firstcomeevent.entity.FirstComeEvent;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Quizzes")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Quiz extends FirstComeEvent {

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

    private Integer winnerCount;

}
