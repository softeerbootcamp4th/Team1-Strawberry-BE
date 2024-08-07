package com.hyundai.softeer.backend.domain.subevent.dto;

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
public class SubEventRequest {
    @NotBlank
    @Parameter
    private long subEventId;
}