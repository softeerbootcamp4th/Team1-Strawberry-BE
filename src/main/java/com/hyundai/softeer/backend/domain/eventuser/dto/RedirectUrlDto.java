package com.hyundai.softeer.backend.domain.eventuser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedirectUrlDto {
    @Schema(example = "api/v1/lottery/drawing/land")
    private String redirectUrl;
}
