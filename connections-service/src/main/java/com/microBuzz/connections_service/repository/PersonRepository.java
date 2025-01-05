package com.microBuzz.connections_service.repository;

import com.microBuzz.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH(a:Person)-[:FOLLOWING]-(b:Person) " +
            "WHERE a.userId= $userId " +
            "RETURN DISTINCT b")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH(a:Person)-[r:FOLLOWING]->(b:Person) "+
            "WHERE a.userId= $currentUserId AND b.userId=$followingUserId "+
            "RETURN count(r)>0")
    Boolean isFollowing(Long currentUserId, Long followingUserId);

    @Query("MATCH (a:Person)-[:FOLLOWING]->(b:Person) " +
            "WHERE b.userId= $currentUserId " +
            "RETURN a")
    List<Person> getFollowers(Long currentUserId);

    @Query("MATCH (a:Person)-[:FOLLOWING]->(b:Person) " +
            "WHERE a.userId= $currentUserId " +
            "RETURN b")
    List<Person> getFollowing(Long currentUserId);

    @Query("MATCH (a:Person)-[r:FOLLOWING]->(b:Person) " +
            "WHERE a.userId= $currentUserId AND b.userId= $followingUserId " +
            "DELETE r")
    Boolean unfollow(Long currentUserId, Long followingUserId);

    @Query("MATCH(a:Person)-[r:FOLLOWING]->(b:Person) " +
            "WHERE b.userId= $currentUserId AND a.userId= $followerUserId " +
            "DELETE r")
    Boolean removeFollower(Long currentUserId, Long followerUserId);

    @Query("MATCH(a:Person),(b:Person) " +
            "WHERE a.userId= $currentUserId AND b.userId= $followUserId " +
            "CREATE (a)-[:FOLLOWING]->(b)")
    Boolean follow(Long currentUserId, Long followUserId);
}
