package com.hyundai.softeer.backend.domain.user.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GoogleUserInfo {
    private String id;
    private String name;
    private String given_name;
    private String family_name;
    private String locale;
    private String email;
    private boolean verified_email;
    private String picture;
    private String hd;
}
