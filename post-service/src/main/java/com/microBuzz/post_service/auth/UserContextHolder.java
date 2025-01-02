package com.microBuzz.post_service.auth;

import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {

    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    public static String getCurrentUserId(){
        return currentUserId.get().toString();
    }

    public static void setCurrentUserId(Long userId){
        currentUserId.set(userId);
    }

    public static void clear(){
        currentUserId.remove();
    }

}
