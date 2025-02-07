package com.microBuzz.user_service.client;

import com.microBuzz.user_service.dto.SignupRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "connections-service", path = "/connections/core")
public interface ConnectionsClient {

    @PostMapping("/newuser")
    void createNewUser(@RequestBody SignupRequestDto signupRequestDto);
}
