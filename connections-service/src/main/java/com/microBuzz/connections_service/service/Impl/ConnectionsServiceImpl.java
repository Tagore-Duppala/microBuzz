package com.microBuzz.connections_service.service.Impl;

import com.microBuzz.connections_service.entity.Person;
import com.microBuzz.connections_service.repository.PersonRepository;
import com.microBuzz.connections_service.service.ConnectionsService;
import com.microBuzz.post_service.auth.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getFirstDegreeConnections(){
        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Fetching first degree connections for user id {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

}
