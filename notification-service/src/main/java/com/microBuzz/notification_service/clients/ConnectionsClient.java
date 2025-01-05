package com.microBuzz.notification_service.clients;

import com.microBuzz.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections/core")
public interface ConnectionsClient {

    @GetMapping("/first-degree")
    List<PersonDto> getMyFirstConnections(@RequestHeader("X-User-Id") Long userId); //Data is passed through kafka so we don't have user id to pass through interceptors

}
