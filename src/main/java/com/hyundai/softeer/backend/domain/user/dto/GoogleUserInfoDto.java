package com.hyundai.softeer.backend.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoogleUserInfoDto extends GoogleUserInfo {
    @Schema(description = "구글 사용자 ID", example = "123456789012345678901")
    private String id;
    @Schema(description = "구글 사용자 이름", example = "name")
    private String name;
    @Schema(description = "구글 사용자 성", example = "given_name")
    private String given_name;
    @Schema(description = "구글 사용자 이름", example = "family_name")
    private String family_name;
    @Schema(description = "구글 사용자 지역", example = "ko")
    private String locale;
    @Schema(description = "구글 사용자 이메일", example = "user01@google.com")
    private String email;
    @Schema(description = "구글 사용자 이메일 인증 여부", example = "true")
    private boolean verified_email;
    @Schema(description = "구글 사용자 프로필 사진", example = "https://googleprofileimage.com")
    private String picture;
    @Schema(description = "callback에서 전달한 uuid로 회원가입 시 전달해야한다.", example = "58effd3e-431b-4ec4-8240-a5636e3a47f7")
    private String uuid;

    public GoogleUserInfoDto(GoogleUserInfo userInfoDto, String uuid) {
        this.id = userInfoDto.getId();
        this.name = userInfoDto.getName();
        this.given_name = userInfoDto.getGiven_name();
        this.family_name = userInfoDto.getFamily_name();
        this.locale = userInfoDto.getLocale();
        this.email = userInfoDto.getEmail();
        this.verified_email = userInfoDto.isVerified_email();
        this.picture = userInfoDto.getPicture();
        this.uuid = uuid;
    }

}