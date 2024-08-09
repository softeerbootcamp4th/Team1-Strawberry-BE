package com.hyundai.softeer.backend.domain.eventuser.entity;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUser {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private Boolean isWriteExpectation;

    private LocalDateTime lastVisitedAt;

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

    private Integer chance;

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
}
