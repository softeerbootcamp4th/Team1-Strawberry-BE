package com.hyundai.softeer.backend.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    @Builder.Default
    @Schema(description = "응답 코드", example = "200")
    private int status = HttpStatus.OK.value();

    @Builder.Default
    @Schema(description = "응답 메시지", example = "OK")
    private String message = HttpStatus.OK.getReasonPhrase();

    @Schema(description = "응답 데이터")
    private T data;

    public BaseResponse(T data) {
        this.data = data;
    }
}
