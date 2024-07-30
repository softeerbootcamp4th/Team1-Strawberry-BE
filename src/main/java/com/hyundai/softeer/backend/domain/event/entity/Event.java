package com.hyundai.softeer.backend.domain.event.entity;

import com.hyundai.softeer.backend.domain.car.entity.Car;
import com.hyundai.softeer.backend.global.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Events")
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private String eventName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime eventRegisteredAt;
    private Integer winnerCount;
    private Boolean eventStatus;
}
