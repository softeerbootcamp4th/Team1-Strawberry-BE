package com.hyundai.softeer.backend.domain.event.entity;

import com.hyundai.softeer.backend.domain.car.entity.Car;
import com.hyundai.softeer.backend.global.constant.ExecuteEnvironment;
import com.hyundai.softeer.backend.global.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private String expectationBannerImgUrl;

    @Profile(ExecuteEnvironment.TEST)
    public static Event testEventGenerator(Long i) {
        return Event.builder()
                .id(i)
                .car(null)
                .eventName("산타페" + i)
                .startAt(LocalDateTime.of(2024,6,24,0,0,0).plusHours(i))
                .endAt(LocalDateTime.of(2024,6,26,0,0,0).plusHours(i))
                .eventRegisteredAt(LocalDateTime.of(2024,6,1,0,0,0).plusHours(i))
                .winnerCount(1)
                .eventStatus(true)
                .expectationBannerImgUrl("www.expect" + i + "com")
                .build();
    }

    @Profile(ExecuteEnvironment.TEST)
    public static List<Event> testEventsGenerator(Long i) {
        List<Event> events = new ArrayList<>();

        for(long cnt = 1L; cnt <= i; cnt++) {
            events.add(Event.testEventGenerator(cnt));
        }

        return events;
    }
}
