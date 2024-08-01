package com.hyundai.softeer.backend.domain.lottery.drawing.dto;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DrawingLotteryLandResponseDto {
    private EventUserInfoDto userInfo;
    private DrawingLotteryLandDto eventInfo;
}
