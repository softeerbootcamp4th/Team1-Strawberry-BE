package com.hyundai.softeer.backend.domain.firstcome.quiz.dto;

import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class QuizFirstComeRegisterRequest {

    @NotNull
    @Parameter
    private Long eventId;

    @NotNull
    @Parameter
    private Integer sequence;

    @NotBlank
    @Parameter
    private String overview;

    @NotBlank
    @Parameter
    private String problem;

    @NotBlank
    @Parameter
    private String carInfo;

    @NotBlank
    @Parameter
    private String answer;

    @NotBlank
    @Parameter
    private String hint;

    @NotBlank
    @Parameter
    private String anchor;

    @NotBlank
    @Parameter
    private String initConsonant;

    @NotNull
    @Parameter
    private Long prizeId;

    @NotNull
    @Parameter
    private Integer winners;

    @NotBlank
    @Parameter
    private String alias;

    @NotNull
    @Parameter
    private SubEventExecuteType executeType;

    @NotNull
    @Parameter
    private SubEventType eventType;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Parameter
    private LocalDateTime startAt;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Parameter
    private LocalDateTime endAt;

    @NotBlank
    @Parameter
    private String bannerImgUrl;

    @Parameter
    private Map<String, Object> eventImgUrls;

    @Parameter
    private Map<String, Object> winnersMeta;
}
