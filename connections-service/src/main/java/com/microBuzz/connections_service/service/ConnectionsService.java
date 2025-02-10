package com.microBuzz.connections_service.service;

import com.microBuzz.connections_service.dto.NewUserDto;
import com.microBuzz.connections_service.dto.PersonDto;
import com.microBuzz.connections_service.entity.Person;

import java.util.List;

public interface ConnectionsService {

    List<Person> getFirstDegreeConnections();

    void createNewUser(NewUserDto newUserDto);

    void followUser(Long followUserId);

    void unfollowUser(Long followingUserId);

    List<PersonDto> getMyFollowers();

    List<PersonDto> getMyFollowing();

    void removeFollower(Long followerUserId);
}
