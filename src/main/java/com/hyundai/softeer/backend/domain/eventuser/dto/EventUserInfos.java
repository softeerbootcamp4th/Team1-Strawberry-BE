package com.hyundai.softeer.backend.domain.eventuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class EventUserInfos {
    private String name;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private Integer isWinner;
}
