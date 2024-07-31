package com.hyundai.softeer.backend.domain.subevent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class BaseSubEvent {
    @Id
    @Column(name = "sub_event_id", nullable = false)
    private Long subEventId;

    private String winners_meta;

    private Integer winners;
}
