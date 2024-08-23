package com.hyundai.softeer.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class ApplicationConfig {
    @Bean
    public Random random() {
        long randomSeed = System.currentTimeMillis();
        return new Random(randomSeed);
    }
}
