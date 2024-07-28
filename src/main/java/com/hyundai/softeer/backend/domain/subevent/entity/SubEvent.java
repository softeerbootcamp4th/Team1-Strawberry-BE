package com.hyundai.softeer.backend.domain.subevent.entity;

import com.hyundai.softeer.backend.domain.event.Event;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "Sub_events")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String alias;

    private SubEventExecuteType executeType;

    private SubEventType eventType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private String bannerUrl;

    private String eventImgUrl;
}
