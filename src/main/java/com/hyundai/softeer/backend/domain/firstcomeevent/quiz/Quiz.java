package com.hyundai.softeer.backend.domain.firstcomeevent.quiz;

import com.hyundai.softeer.backend.domain.event.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Quizzes")
public class Quiz {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long quizId;

  private Integer sequence;

  private String problem;

  private String answer;

  @JoinColumn(name = "event_id")
  @ManyToOne
  private Event event;
}
