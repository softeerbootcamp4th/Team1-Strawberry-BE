package com.hyundai.softeer.backend.domain.subevent.entity;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventExecuteType;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.global.constant.ExecuteEnvironment;
import com.hyundai.softeer.backend.global.dto.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Sub_events")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String alias;

    private SubEventExecuteType executeType;

    private SubEventType eventType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String bannerImgUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> eventImgUrls;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> winnersMeta;

    @Profile(ExecuteEnvironment.TEST)
    public static SubEvent subEventGenerator(Long i) {
        return SubEvent.builder()
                .id(i)
                .event(Event.testEventGenerator(i))
                .alias("산타페 이벤" + i)
                .startAt(LocalDateTime.of(2024, 06, 24, 0, 0, 0).plusHours(i))
                .endAt(LocalDateTime.of(2024, 06, 26, 0, 0, 0).plusHours(i))
                .eventType(SubEventType.QUIZ)
                .executeType(SubEventExecuteType.FIRSTCOME)
                .eventImgUrls(null)
                .bannerImgUrl("www.banner" + i + ".com")
                .build();
    }

    @Profile(ExecuteEnvironment.TEST)
    public static List<SubEvent> subEventsGenerator(Long i) {
        List<SubEvent> subEvents = new ArrayList<>();

        for (long cnt = 1; cnt <= i; cnt++) {
            subEvents.add(subEventGenerator(cnt));
        }

        return subEvents;
    }
}
