package com.hyundai.softeer.backend.domain.user.entity;

import com.hyundai.softeer.backend.domain.user.dto.CreateGoogleUserRequest;
import com.hyundai.softeer.backend.domain.user.dto.GoogleUserInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class User {
    @Id
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String name;
    private String nickname;
    private LocalDate birthdate;
    private UserRole userRole;
    private String profileImageUrl;
    private SignUpType signUpType;

    public User(CreateGoogleUserRequest dto, GoogleUserInfo userInfo) {
        this.email = userInfo.getEmail();
        this.nickname = dto.getNickname();
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.birthdate = LocalDate.parse(dto.getBirthdate());
        this.userRole = UserRole.USER;
        this.profileImageUrl = userInfo.getPicture();
        this.signUpType = SignUpType.Google;
    }
}
