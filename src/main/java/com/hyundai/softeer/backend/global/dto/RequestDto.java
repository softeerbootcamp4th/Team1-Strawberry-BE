package com.hyundai.softeer.backend.global.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json 형태로 변환 가능한 RequestDto
 */
public class RequestDto {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
