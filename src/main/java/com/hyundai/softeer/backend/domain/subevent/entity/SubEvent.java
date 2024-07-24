package com.hyundai.softeer.backend.domain.subevent.entity;

import com.hyundai.softeer.backend.domain.lottery.entity.DrawingLottery;
import com.hyundai.softeer.backend.domain.lottery.entity.Lottery;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sub_events")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alias;

    private SubEventExecuteType executeType;

    private SubEventType eventType;

    private String winnersMeta;

    private String winners;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
    
}
