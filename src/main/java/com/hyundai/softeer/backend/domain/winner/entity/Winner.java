package com.hyundai.softeer.backend.domain.winner.entity;

import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "Winners")
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Winner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prize prize;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubEvent subEvent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    private int ranking;

    public Winner(Prize prize, SubEvent subEvent, User user, int rank) {
        this.prize = prize;
        this.subEvent = subEvent;
        this.user = user;
        this.ranking = rank;
    }
}