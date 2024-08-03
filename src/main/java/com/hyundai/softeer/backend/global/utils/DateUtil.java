package com.hyundai.softeer.backend.global.utils;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DateUtil {

    private final Clock clock;
    public boolean isWithinSubEventPeriod(SubEvent subEvent) {
        LocalDateTime current = LocalDateTime.now(clock);
        LocalDateTime startAt = subEvent.getStartAt();
        LocalDateTime endAt = subEvent.getEndAt();

        return current.isAfter(startAt) && current.isBefore(endAt);
    }

    public boolean isNotWithinSubEventPeriod(SubEvent subEvent) {
        return !isWithinSubEventPeriod(subEvent);
    }

    public long startBetweenCurrentDiff(SubEvent subEvent) {
        LocalDateTime current = LocalDateTime.now(clock);
        LocalDateTime startAt = subEvent.getStartAt();
        Duration duration = Duration.between(current, startAt);
        return Math.max(0, duration.getSeconds());
    }

    public boolean eventNotWithinPeriod(Event event) {
        LocalDateTime current = LocalDateTime.now(clock);
        return current.isBefore(event.getStartAt()) || current.isAfter(event.getEndAt());
    }

    public SubEvent findClosestSubEvent(List<SubEvent> subEvents) {
        LocalDateTime current = LocalDateTime.now(clock);
        SubEvent closetSubEvent = null;
        for(SubEvent subEvent: subEvents) {
            if(subEvent.getEventType().equals(SubEventType.DRAWING)) continue;

            if(isWithinSubEventPeriod(subEvent)) {
                return subEvent;
            }

            if(current.isBefore(subEvent.getEndAt())) {
                return subEvent;
            }
        }
        return closetSubEvent;
    }
}
