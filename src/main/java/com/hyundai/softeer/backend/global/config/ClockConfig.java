package com.hyundai.softeer.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * LocalDateTime.now()를 테스트 하기 위해 LocalDateTime.now(Clock clock)을 사용합니다.
 * clock을 주입하
 */
@Configuration
public class ClockConfig {
    @Bean
    public Clock clock() {
       return Clock.systemDefaultZone();
    }
}
