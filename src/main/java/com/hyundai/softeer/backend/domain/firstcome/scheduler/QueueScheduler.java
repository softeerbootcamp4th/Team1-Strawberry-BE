package com.hyundai.softeer.backend.domain.firstcome.scheduler;

import com.hyundai.softeer.backend.domain.firstcome.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final long POP_CNT = 750;

    private final QueueService queueService;

    @Scheduled(fixedDelay = 10000)
    private void chickenEventScheduler() {
        Set<String> tokens = queueService.popTokensFromWaitingQueue(POP_CNT);
        queueService.addTokensToWorkingQueue(tokens);
        log.info("Scheduler is running. {} tokens are moved to working queue.", tokens.size());
    }
}
