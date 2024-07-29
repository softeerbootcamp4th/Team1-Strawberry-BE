package com.hyundai.softeer.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class NaverUserInfo {
    private String name;
    private String email;
    private String birthday;
    private String birthyear;
    private String mobile;
}