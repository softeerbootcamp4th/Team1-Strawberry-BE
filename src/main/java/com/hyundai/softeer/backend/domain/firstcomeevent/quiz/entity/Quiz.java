package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity;

import com.hyundai.softeer.backend.domain.event.Event;
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

  private String problem;

  private String answer;

  private String hint;

  /**
   * 추후 결정
   */
  private String anchor;
}
