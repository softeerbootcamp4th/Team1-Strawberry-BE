package com.hyundai.softeer.backend.domain.eventuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SharedUrlRequest {
    @NotBlank
    private String sharedUrl;
}
