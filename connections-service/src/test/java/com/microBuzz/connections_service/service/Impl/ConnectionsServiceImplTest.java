package com.microBuzz.connections_service.service.Impl;

import com.microBuzz.connections_service.auth.UserContextHolder;
import com.microBuzz.connections_service.entity.Person;
import com.microBuzz.connections_service.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ConnectionsServiceImplTest {

    @Mock
    private PersonRepository personRepository;


    private Long userId= 1L;

    @InjectMocks
    private ConnectionsServiceImpl connectionsService;

    private List<Person> mockPersons;

    Long id = 1L;

    @BeforeEach
    void setup(){

        UserContextHolder.setCurrentUserId(userId);
        mockPersons = List.of(Person.builder()
                .id(1L)
                .email("tagore@gmail.com")
                .name("Tagore")
                .bio("null")
                .userId(2L)
                .build());
        System.out.println("current userid: "+ UserContextHolder.getCurrentUserId());
    }

    @Test
    void testGetFirstDegreeConnections_ReturnListOfPersons(){


//        when(personRepository.getFirstDegreeConnections(userId)).thenReturn(Mono.just(mockPersons));

        List<Person> finalList = connectionsService.getFirstDegreeConnections();

        Assertions.assertThat(finalList.size()).isEqualTo(mockPersons.size());

    }

    @AfterEach
    void cleanup() {
        // Clear the ThreadLocal after each test to avoid affecting other tests
        UserContextHolder.clear();
    }

}