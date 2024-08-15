package com.hyundai.softeer.backend.domain.subevent.dto;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class SubEventInfo {
    private String alias;

    private SubEventExecuteType executeType;

    private SubEventType eventType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String bannerImgUrl;

    public static SubEventInfo fromEntity(SubEvent subEvent) {
        return SubEventInfo.builder()
                .alias(subEvent.getAlias())
                .bannerImgUrl(subEvent.getBannerImgUrl())
                .eventType(subEvent.getEventType())
                .executeType(subEvent.getExecuteType())
                .startAt(subEvent.getStartAt())
                .endAt(subEvent.getEndAt())
                .bannerImgUrl(subEvent.getBannerImgUrl())
                .build();
    }
}
