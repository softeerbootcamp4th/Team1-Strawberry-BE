package com.hyundai.softeer.backend.domain.firstcome.scheduler;

import com.hyundai.softeer.backend.domain.firstcome.service.QueueService;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final long POP_CNT = 200;
    private final List<Long> subEventIds = new ArrayList<>();

    private final QueueService queueService;
    private final SubEventRepository subEventRepository;

    @Value("${properties.event-id}")
    private Long eventId;

    @PostConstruct
    private void init() {
        subEventRepository.findByEventId(eventId)
                .forEach(subEvent -> subEventIds.add(subEvent.getId()));
    }

    @Scheduled(fixedDelay = 1000)
    private void EventScheduler() {
        for (Long subEventId : subEventIds) {
            Set<String> tokens = queueService.popTokensFromWaitingQueue(subEventId, POP_CNT);
            queueService.addTokensToWorkingQueue(tokens);
            log.info("waiting queue pop tokens size from subEventId: {} is {}", subEventId, tokens.size());
        }
    }
}
