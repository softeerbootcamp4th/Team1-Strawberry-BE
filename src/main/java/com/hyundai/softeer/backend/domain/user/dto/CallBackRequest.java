package com.hyundai.softeer.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Data
@NoArgsConstructor
@ParameterObject
public class CallBackRequest {
    @NotBlank
    private String code;

    @NotBlank
    private String state;
}
