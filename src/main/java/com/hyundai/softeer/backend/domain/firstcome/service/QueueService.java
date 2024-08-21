package com.hyundai.softeer.backend.domain.firstcome.service;

import com.hyundai.softeer.backend.domain.firstcome.dto.QueueRequest;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {
    private final RedisTemplate<String, String> redisTemplate;
    private final String WAIT_QUEUE_KEY = "waiting:";
    private final String WORK_QUEUE_KEY = "working:";


    public String createToken(User authenticatedUser, QueueRequest queueRequest) {
        String combinedData = authenticatedUser.getId() + ":"
                + queueRequest.getSubEventId() + ":"
                + UUID.randomUUID().toString();
        return Base64.getEncoder().encodeToString(combinedData.getBytes());
    }

    public String addWaitingQueue(String token) {
        redisTemplate.opsForZSet().add(WAIT_QUEUE_KEY, token, System.currentTimeMillis());
        redisTemplate.expire(WAIT_QUEUE_KEY, 60 * 60, TimeUnit.SECONDS);
        return token;
    }

    public long getCurrentUserInWaitingQueue() {
        Long count = redisTemplate.opsForZSet().zCard(WAIT_QUEUE_KEY);
        return count != null ? count : 0;
    }

    public long getUserCountWithLowerTimestamp(String token) {
        Long rank = redisTemplate.opsForZSet().rank(WAIT_QUEUE_KEY, token);

        return rank != null ? rank : 0;
    }

    public Set<String> popTokensFromWaitingQueue(long count) {
        Set<String> tokens = redisTemplate.opsForZSet().range(WAIT_QUEUE_KEY, 0, count - 1);

        if (tokens != null && !tokens.isEmpty()) {
            redisTemplate.opsForZSet().remove(WAIT_QUEUE_KEY, tokens.toArray());
        }

        return tokens;
    }

    public void addTokensToWorkingQueue(Set<String> tokens) {
        tokens.forEach(token -> redisTemplate.opsForZSet().add(WORK_QUEUE_KEY, token, System.currentTimeMillis()));
    }

}
