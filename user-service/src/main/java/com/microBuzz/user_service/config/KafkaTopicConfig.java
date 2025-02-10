package com.microBuzz.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userOnboarding(){
        return new NewTopic("user-onboarding-topic", 3, (short) 1);
    }

}
