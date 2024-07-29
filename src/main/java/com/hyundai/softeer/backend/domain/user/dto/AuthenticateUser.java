package com.hyundai.softeer.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AuthenticateUser {
    private String email;
}
