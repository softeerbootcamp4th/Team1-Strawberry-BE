package com.hyundai.softeer.backend.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class QuizStartInfo {
    Boolean isStarted;

    LocalDateTime startAt;
}
