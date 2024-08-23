package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PreviewRequest {
    @NotBlank
    @Size(min = 6, max = 6, message = "사용자 ID는 정확히 6글자여야 합니다.")
    String sharedUrl;
}
