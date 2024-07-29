package com.hyundai.softeer.backend.domain.user.entity;

import com.hyundai.softeer.backend.global.jwt.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String phoneNumber;

    private LocalDate birthDate;

    private OAuthProvider oAuthProvider;

    @Builder
    public User(String email, String name,String phoneNumber, LocalDate birthDate, OAuthProvider oAuthProvider) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.oAuthProvider = oAuthProvider;
    }
}
