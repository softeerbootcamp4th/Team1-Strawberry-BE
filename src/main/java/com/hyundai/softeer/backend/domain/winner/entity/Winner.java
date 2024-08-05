package com.hyundai.softeer.backend.domain.winner.entity;

import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Table(name = "Winners")
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


    public Winner(Prize prize, SubEvent subEvent, User user) {
        this.prize = prize;
        this.subEvent = subEvent;
        this.user = user;
    }
}