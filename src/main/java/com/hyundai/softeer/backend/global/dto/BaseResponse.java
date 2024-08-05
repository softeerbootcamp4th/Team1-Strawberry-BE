package com.hyundai.softeer.backend.global.dto;

import com.hyundai.softeer.backend.global.status.BaseResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    @Builder.Default
    @Schema(description = "응답 코드")
    private int status = BaseResponseStatus.SUCCESS.getStatus();

    @Builder.Default
    @Schema(description = "응답 메시지")
    private String message = BaseResponseStatus.SUCCESS.getMessage();

    @Schema(description = "응답 데이터")
    private T data;
}
