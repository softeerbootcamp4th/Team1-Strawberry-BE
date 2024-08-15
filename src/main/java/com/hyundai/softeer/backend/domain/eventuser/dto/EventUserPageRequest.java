package com.hyundai.softeer.backend.domain.eventuser.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@Data
@AllArgsConstructor
@ParameterObject
public class EventUserPageRequest {
    @Parameter
    @NotNull
    private Integer pageSequence;

    @Parameter
    @NotNull
    private Long eventId;
}
