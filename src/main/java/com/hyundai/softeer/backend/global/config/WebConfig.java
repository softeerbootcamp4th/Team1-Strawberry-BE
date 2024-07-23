package com.hyundai.softeer.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.softeer.backend.global.jwt.provider.TokenProvider;
import com.hyundai.softeer.backend.global.filter.JwtAuthorizationFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class WebConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Bean
    public FilterRegistrationBean jwtAuthorizationFilter(TokenProvider provider, ObjectMapper mapper) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtAuthorizationFilter(provider,mapper));
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}