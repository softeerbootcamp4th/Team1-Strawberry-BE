package com.hyundai.softeer.backend.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisSetService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToSet(String key, String... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public Set<String> getAllMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public String getRandomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    public String popRandomMember(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    public Long removeFromSet(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public void removeAll(String key) {
        redisTemplate.delete(key);
    }
}
