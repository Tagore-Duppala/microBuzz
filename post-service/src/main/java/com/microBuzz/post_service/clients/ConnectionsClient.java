package com.microBuzz.post_service.clients;

import com.microBuzz.post_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections/core")
public interface ConnectionsClient {

    @GetMapping("/first-degree")
    List<PersonDto> getMyFirstConnections();

}
