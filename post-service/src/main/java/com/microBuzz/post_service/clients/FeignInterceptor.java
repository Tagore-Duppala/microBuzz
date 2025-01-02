package com.microBuzz.post_service.clients;

import com.microBuzz.post_service.auth.UserContextHolder;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> requestTemplate.header("X-User-Id", UserContextHolder.getCurrentUserId());
    }
}
