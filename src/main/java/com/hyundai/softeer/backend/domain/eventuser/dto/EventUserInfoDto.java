package com.hyundai.softeer.backend.domain.eventuser.dto;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventUserInfoDto {
    @Schema(description = "마지막 방문 시간", example = "2021-08-01T00:00:00")
    private LocalDateTime lastVisitedAt;

    @Schema(description = "공유 점수", example = "3.0")
    private Double sharedScore;

    @Schema(description = "우선순위 점수", example = "10.0")
    private Double priorityScore;

    @Schema(description = "추첨 점수", example = "3.2")
    private Double lottoScore;

    @Schema(description = "게임 점수", example = "99.3")
    private Double gameScore;

    @Schema(description = "참여 기회", example = "1")
    private Integer chance;

    @Schema(description = "기대평 보너스 참여 기회", example = "1")
    private Integer expectationBonusChance;

    @Schema(description = "공유 보너스 참여 기회", example = "-1")
    private Integer shareBonusChance;

    @Schema(description = "당첨 여부", example = "true")
    private boolean isWinner;

    public static EventUserInfoDto fromEntity(EventUser eventUser) {
        return EventUserInfoDto.builder()
                .lastVisitedAt(eventUser.getLastVisitedAt())
                .sharedScore(eventUser.getSharedScore())
                .priorityScore(eventUser.getPriorityScore())
                .lottoScore(eventUser.getLottoScore())
                .gameScore(eventUser.getGameScore())
                .chance(eventUser.getChance())
                .expectationBonusChance(eventUser.getExpectationBonusChance())
                .shareBonusChance(eventUser.getShareBonusChance())
                .isWinner(eventUser.getIsWinner())
                .build();
    }
}
