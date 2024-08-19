package com.hyundai.softeer.backend.domain.firstcome.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService {

    public static final String COUNTER_KEY = "COUNTER";

    private final RedisTemplate<String, String> redisTemplate;

    public Long incrementCounter(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long decrementCounter(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long getCounterValue(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return (value != null) ? Long.parseLong(value) : 0L;
    }

    public void resetCounter(String key) {
        redisTemplate.opsForValue().set(key, "0");
    }

    public void setCounter(String key, long value) {
        redisTemplate.opsForValue().set(key, String.valueOf(value));
    }
}
