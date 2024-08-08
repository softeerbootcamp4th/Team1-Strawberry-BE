package com.hyundai.softeer.backend.domain.event.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@AllArgsConstructor
@ParameterObject
public class ApiKeyRequest {
    @NotBlank
    @Parameter
    private String apiKey;

    public boolean validateApiKey(String apiKeySecret) {
        if (apiKey.equals(apiKeySecret)) {
            return true;
        }
        return false;
    }
}