package com.microBuzz.user_service.event;

import lombok.Builder;
import lombok.Data;

@Data
public class UserOnboardingEvent {

    private String email;
    private Long userId;
    private String name;

}
