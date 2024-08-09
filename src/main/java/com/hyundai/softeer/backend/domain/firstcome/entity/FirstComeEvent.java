package com.hyundai.softeer.backend.domain.firstcome.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class FirstComeEvent {
    @Id
    @Column(name = "sub_event_id", nullable = false)
    private Long subEventId;

    private Integer winners;
}
