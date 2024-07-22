package com.hyundai.softeer.backend.domain.user.dto;

import com.hyundai.softeer.backend.global.dto.RequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest extends RequestDto {
    @NotBlank
    @Email
    @Schema(description = "사용자 이메일", example = "user01@email.com")
    private final String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Schema(description = "사용자 비밀번호", example = "password123")
    private final String password;
}
