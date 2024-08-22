package com.hyundai.softeer.backend.domain.firstcome.service;

import com.hyundai.softeer.backend.domain.firstcome.dto.WaitingEnqueueBodyRequest;
import com.hyundai.softeer.backend.domain.firstcome.exception.WaitingInvalidTokenException;
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


    public String createToken(User authenticatedUser, WaitingEnqueueBodyRequest waitingEnqueueBodyRequest) {
        String combinedData = authenticatedUser.getId() + ":"
                + waitingEnqueueBodyRequest.getSubEventId() + ":"
                + UUID.randomUUID().toString();
        return Base64.getEncoder().encodeToString(combinedData.getBytes()).replaceAll("=+$", "");
    }

    public String addWaitingQueue(long subEventId, String token) {
        redisTemplate.opsForZSet().add(WAIT_QUEUE_KEY + subEventId, token, System.currentTimeMillis());
        redisTemplate.expire(WAIT_QUEUE_KEY, 60 * 60, TimeUnit.SECONDS);
        return token;
    }

    public long getCurrentUserInWaitingQueue(long subEventId) {
        Long count = redisTemplate.opsForZSet().zCard(WAIT_QUEUE_KEY + subEventId);
        return count != null ? count : 0;
    }

    public long getUserCountWithLowerTimestamp(long subEventId, String token) {
        Long rank = redisTemplate.opsForZSet().rank(WAIT_QUEUE_KEY + subEventId, token);

        return rank != null ? rank + 1 : 0;
    }

    public Set<String> popTokensFromWaitingQueue(long subEventId, long count) {
        Set<String> tokens = redisTemplate.opsForZSet().range(WAIT_QUEUE_KEY + subEventId, 0, count - 1);

        if (tokens != null && !tokens.isEmpty()) {
            redisTemplate.opsForZSet().remove(WAIT_QUEUE_KEY + subEventId, tokens.toArray());
        }

        return tokens;
    }

    public void addTokensToWorkingQueue(Set<String> tokens) {
        tokens.forEach(token -> {
            redisTemplate.opsForValue().set(token, "working", 30, TimeUnit.MINUTES);
        });
    }

    public void validateToken(String token, Long id, Long subEventId) {
        String[] decodedToken = new String(Base64.getDecoder().decode(token)).split(":");
        if (decodedToken.length != 3 ||
                !decodedToken[0].equals(id.toString()) ||
                !decodedToken[1].equals(subEventId.toString())) {
            throw new WaitingInvalidTokenException();
        }

        if (redisTemplate.opsForValue().get(token) == null) {
            throw new WaitingInvalidTokenException();
        }
    }
}
