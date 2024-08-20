package com.hyundai.softeer.backend.domain.eventuser.entity;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "event_users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Builder.Default
    private Boolean isWriteExpectation = false;

    @Builder.Default
    private Boolean isWinner = false;

    @Builder.Default
    private LocalDateTime lastVisitedAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastChargeAt = LocalDateTime.now();

    @Column(unique = true)
    private String sharedUrl;

    @Builder.Default
    private Double sharedScore = 0.0;

    @Builder.Default
    private Double priorityScore = 0.0;

    @Builder.Default
    private Double lottoScore = 0.0;

    @Builder.Default
    private Double gameScore = 0.0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    @Builder.Default
    private Map<String, Object> scores = new HashMap<>();

    @Builder.Default
    private Integer chance = 2;

    @Builder.Default
    private Integer expectationBonusChance = -1;

    @Builder.Default
    private Integer shareBonusChance = -1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_event_id", nullable = false)
    private SubEvent subEvent;

    public void updateSharedUrl(String sharedUrl) {
        this.sharedUrl = sharedUrl;
        this.shareBonusChance = 1;
    }

    public void updateScores(String key, Object value) {
        this.scores.put(key, value);
    }

    public void updateGameScore(Double gameScore) {
        this.gameScore = gameScore;
    }

    public void updateExpectationBonusChanceIfNotUsed() {
        if (this.expectationBonusChance == -1) {
            this.expectationBonusChance = 1;
        }
    }

    public void scoreSharedScore() {
        this.sharedScore += 1;
    }

    public void updateLastVisitedAtAndLastChargeAt() {
        this.lastVisitedAt = LocalDateTime.now();

        long diff = Duration.between(this.lastChargeAt, this.lastVisitedAt).toHours();

        if (diff >= 4 && diff < 8) {
            // 4시간 이상 8시간 미만 시 충전 시간을 4시간 뒤로 설정
            this.lastChargeAt = this.lastChargeAt.plusHours(4);
            // 기회 1회 추가
            this.chance = Math.min(2, this.chance + 1);
        } else if (diff >= 8) {
            // 8시간 이상 시 충전 시간을 현재 시간으로 설정
            this.lastChargeAt = this.lastVisitedAt;
            // 기회 2회 추가
            this.chance = Math.min(2, this.chance + 2);
        }
    }

    public void useChance() {
        this.chance -= 1;
    }

    public void useExpectationBonusChance() {
        this.expectationBonusChance -= 1;
    }

    public void useShareBonusChance() {
        this.shareBonusChance -= 1;
    }

    public void updateWinner() {
        this.isWinner = true;
    }
}
