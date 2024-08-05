package com.hyundai.softeer.backend.global.dto;

import com.hyundai.softeer.backend.global.status.BaseResponseStatus;
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
    private int status = BaseResponseStatus.SUCCESS.getStatus();

    @Builder.Default
    private String message = BaseResponseStatus.SUCCESS.getMessage();

    private T data;

    public BaseResponse(T data) {
        this.data = data;
    }
}
