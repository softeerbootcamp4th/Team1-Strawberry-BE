package com.hyundai.softeer.backend.domain.eventuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventUserInfoRequest {

    @NotBlank
    private Long subEventId;
}
