package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class DrawingLotteryLandDto {
    @Schema(description = "배너 이미지 url", example = "https://www.example.com/banner.jpg")
    private String bannerImgUrl;

    //TODO : 파싱을 해서 주어야하는 지, 확인
    @Schema(description = "이벤트 이미지 url", example = "{ \"description_url\": \"https://www.example.com/description.jpg\", \"main_url\": \"https://www.example.com/main.jpg\" }")
    private Map<String, Object> eventImgUrls;

    @Schema(description = "이벤트 시작 시간", example = "2021-08-01T00:00:00")
    private LocalDateTime startAt;

    @Schema(description = "이벤트 종료 시간", example = "2021-08-30T00:00:00")
    private LocalDateTime endAt;

    public static DrawingLotteryLandDto fromEntity(SubEvent subEvent) {
        return DrawingLotteryLandDto.builder()
                .bannerImgUrl(subEvent.getBannerImgUrl())
                .eventImgUrls(subEvent.getEventImgUrls())
                .startAt(subEvent.getStartAt())
                .endAt(subEvent.getEndAt())
                .build();
    }
}
