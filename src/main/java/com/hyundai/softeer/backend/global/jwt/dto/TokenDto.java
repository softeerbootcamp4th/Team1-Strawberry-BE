package com.hyundai.softeer.backend.global.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}