package com.hyundai.softeer.backend.domain.user.dto;

import com.hyundai.softeer.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    @Schema(description = "사용자 고유 번호", example = "1")
    private Long id;
    @Schema(description = "사용자 이메일", example = "user01@email.com")
    private String email;
    @Schema(description = "사용자 닉네임", example = "nickname01")
    private String nickname;
    @Schema(description = "사용자 이름", example = "name01")
    private String name;
    @Schema(description = "생년월일", example = "2000-01-01")
    private LocalDate birthdate;

    public static UserInfoDto fromEntity(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .build();
    }
}
