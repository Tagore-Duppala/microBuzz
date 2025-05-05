package com.microBuzz.api_gateway.config;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(10)  // 10 requests per second
                .limitRefreshPeriod(java.time.Duration.ofSeconds(60))  // Refresh per second
                .timeoutDuration(java.time.Duration.ofMillis(500))  // Timeout duration for acquiring permission
                .build();

        // Create a RateLimiterRegistry with the configured RateLimiter
        return RateLimiterRegistry.of(rateLimiterConfig);
    }
}


