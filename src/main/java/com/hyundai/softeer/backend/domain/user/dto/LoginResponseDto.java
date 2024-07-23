package com.hyundai.softeer.backend.domain.user.dto;

import com.hyundai.softeer.backend.global.authentication.domain.TokenDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    UserInfoDto user;
    TokenDto token;
}
