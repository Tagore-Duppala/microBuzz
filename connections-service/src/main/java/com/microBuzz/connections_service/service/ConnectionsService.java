package com.microBuzz.connections_service.service;

import com.microBuzz.connections_service.entity.Person;

import java.util.List;

public interface ConnectionsService {

    List<Person> getFirstDegreeConnections(Long userId);
}
