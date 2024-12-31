package com.microBuzz.connections_service.repository;

import com.microBuzz.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH(a:Person)-[:CONNECTED_TO]-(b:Person) " +
            "WHERE a.userId= $userId " +
            "RETURN DISTINCT b")
    List<Person> getFirstDegreeConnections(Long userId);
}
