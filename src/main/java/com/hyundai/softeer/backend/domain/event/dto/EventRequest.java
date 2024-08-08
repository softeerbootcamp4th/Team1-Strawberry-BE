package com.hyundai.softeer.backend.domain.event.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Data
@NoArgsConstructor
@ParameterObject
public class EventRequest {
    @NotBlank
    @Parameter
    private long eventId;
}