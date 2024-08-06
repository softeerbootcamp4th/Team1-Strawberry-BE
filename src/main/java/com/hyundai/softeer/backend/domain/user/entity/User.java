package com.hyundai.softeer.backend.domain.user.entity;

import com.hyundai.softeer.backend.global.dto.BaseEntity;
import com.hyundai.softeer.backend.global.jwt.OAuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "Users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String phoneNumber;

    private LocalDate birthDate;

    private OAuthProvider oAuthProvider;

}
