package com.hyundai.softeer.backend.domain.firstcome.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
public class WaitingEnqueueBodyRequest {
    Long subEventId;
}
