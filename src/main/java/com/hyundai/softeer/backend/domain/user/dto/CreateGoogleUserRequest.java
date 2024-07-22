package com.hyundai.softeer.backend.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateGoogleUserRequest {
    @NotBlank
    @Size(max = 20)
    @Schema(description = "사용자 닉네임", example = "nickname01")
    private String nickname;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "사용자 이름", example = "name01")
    private String name;

    @Size(max = 20)
    @Schema(description = "사용자 전화번호", example = "+01012345678")
    private String phone;

    @Size(max = 100)
    @Schema(description = "사용자 위치", example = "Seoul")
    private String location;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "사용자 생년월일", example = "1990-01-01")
    private String birthdate;

    @Schema(description = "callback에서 전달한 uuid", example = "58effd3e-431b-4ec4-8240-a5636e3a47f7")
    private String uuid;
}
