package com.hyundai.softeer.backend.domain.event.service;

import com.hyundai.softeer.backend.domain.event.dto.ApiKeyRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    @Hidden
    @Value("${api.key}")
    private String apiKeySecret;

    public boolean validateApiKey(ApiKeyRequest apiKeyRequest) {
        return apiKeyRequest.validateApiKey(apiKeySecret);
    }
}
