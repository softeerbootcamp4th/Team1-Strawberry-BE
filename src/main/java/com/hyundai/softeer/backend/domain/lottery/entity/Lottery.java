package com.hyundai.softeer.backend.domain.lottery.entity;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor
public class Lottery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
