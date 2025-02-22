package com.microBuzz.connections_service.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node
@Getter
@Setter
@Builder
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Property("name")
    private String name;

    private Long userId;

    private String email;

    private String bio;
}
