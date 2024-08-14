package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import com.hyundai.softeer.backend.domain.subevent.enums.EventPlayType;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class DrawingInfoRequest {
    @NotNull
    @Parameter
    private Long subEventId;

    @NotNull
    @Parameter
    private EventPlayType eventPlayType;
}