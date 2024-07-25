package com.hyundai.softeer.backend.domain.lottery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor
public class Lottery {
    @Id
    @Column(name = "sub_event_id", nullable = false)
    private Long subEventId;

    private String winners_meta;

    private String winners;
}
