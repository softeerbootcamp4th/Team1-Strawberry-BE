package com.hyundai.softeer.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Builder
@Getter
public class HyundaiUserInfo {
    private String name;
    private String email;
    private LocalDate birthdate;
    private String mobileNum;
}