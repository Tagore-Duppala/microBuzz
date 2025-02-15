package com.microBuzz.api_gateway.filters;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class RateLimitFilter extends AbstractGatewayFilterFactory<Object> {

    private final RateLimiter rateLimiter;

    public RateLimitFilter(RateLimiterRegistry rateLimiterRegistry){ //RateLimiterRegistry is used to find instances of resilience
        this.rateLimiter = rateLimiterRegistry.rateLimiter("apiRateLimiter");
    }


    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

                if (rateLimiter.acquirePermission()) {
                    // Proceed with the request if allowed
                    return chain.filter(exchange);
                } else {
                    // Reject the request if the rate limit is exceeded
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
                    return exchange.getResponse().setComplete();
                }
        };
    }
}
