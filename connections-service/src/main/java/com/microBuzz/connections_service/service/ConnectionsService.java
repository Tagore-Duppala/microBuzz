package com.microBuzz.connections_service.service;

import com.microBuzz.connections_service.dto.PersonDto;

import java.util.List;

public interface ConnectionsService {

    List<PersonDto> getFirstDegreeConnections();

    void followUser(Long followUserId);
    
    void unfollowUser(Long followingUserId);

    List<PersonDto> getMyFollowers();

    List<PersonDto> getMyFollowing();

    void removeFollower(Long followerUserId);
}
