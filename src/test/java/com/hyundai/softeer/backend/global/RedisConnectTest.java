package com.hyundai.softeer.backend.global;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisConnectTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("레디스 연결을 테스트 합니다.")
    void simpleConnectTest() {
        redisTemplate.opsForValue().set("minjun", "0");
        redisTemplate.opsForValue().increment("minjun");
        String s = redisTemplate.opsForValue().get("minjun");
        Assertions.assertThat(s).isEqualTo("1");
    }

}
