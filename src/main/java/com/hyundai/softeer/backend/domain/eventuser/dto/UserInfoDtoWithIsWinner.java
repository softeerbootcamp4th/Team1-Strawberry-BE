package com.hyundai.softeer.backend.domain.eventuser.dto;

import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class UserInfoDtoWithIsWinner {
    private Long userId;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private boolean isWinner;

    public static UserInfoDtoWithIsWinner fromEntity(User user, boolean isWinner) {
       return UserInfoDtoWithIsWinner.builder()
               .name(user.getName())
               .phoneNumber(user.getPhoneNumber())
               .birthDate(user.getBirthDate())
               .email(user.getEmail())
               .isWinner(isWinner)
               .build();
    }
}
