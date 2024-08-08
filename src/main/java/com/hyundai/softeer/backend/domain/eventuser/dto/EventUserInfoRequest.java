package com.hyundai.softeer.backend.domain.eventuser.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Data
@NoArgsConstructor
@ParameterObject
public class EventUserInfoRequest {

    @Parameter
    @NotNull
    private Long subEventId;
}
