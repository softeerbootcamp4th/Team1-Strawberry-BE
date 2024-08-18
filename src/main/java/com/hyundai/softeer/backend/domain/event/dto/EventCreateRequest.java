package com.hyundai.softeer.backend.domain.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class EventCreateRequest {
    private Long carId;
    private String eventName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Boolean eventStatus;
    private MultipartFile bannerImg;
    private Map<String, MultipartFile> images;
}
