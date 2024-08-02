package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity;

import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.subevent.entity.BaseSubEvent;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="Quizzes")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Quiz extends BaseSubEvent {

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
