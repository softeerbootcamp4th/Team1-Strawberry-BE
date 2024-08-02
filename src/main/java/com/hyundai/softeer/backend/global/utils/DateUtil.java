package com.hyundai.softeer.backend.global.utils;

import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

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
}
