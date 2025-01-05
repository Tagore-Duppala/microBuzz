package com.microBuzz.connections_service.service.Impl;

import com.microBuzz.connections_service.dto.PersonDto;
import com.microBuzz.connections_service.entity.Person;
import com.microBuzz.connections_service.repository.PersonRepository;
import com.microBuzz.connections_service.service.ConnectionsService;
import com.microBuzz.post_service.auth.UserContextHolder;
import com.microBuzz.post_service.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PersonDto> getFirstDegreeConnections(){
        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Fetching first degree connections for user id {}", userId);
        List<Person> firstDegree=  personRepository.getFirstDegreeConnections(userId);

        return firstDegree.stream()
                .map(firstDegree1-> modelMapper.map(firstDegree1, PersonDto.class))
                .toList();
    }

    @Override
    public void followUser(Long followUserId) {

        Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Current user is: {}",currentUserId);

        log.info("Checking the if user already follows");
        Boolean isFollowing= personRepository.isFollowing(currentUserId, followUserId);

        if(isFollowing) throw new BadRequestException("You are already following user: "+followUserId);
        personRepository.follow(currentUserId,followUserId);
        log.info("You are now following: {}",followUserId);
    }

    @Override
    public void unfollowUser(Long followingUserId) {

        Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Current user is: {}",currentUserId);

        log.info("Checking the if Current user is following {}",followingUserId);
        Boolean isFollowing= personRepository.isFollowing(currentUserId,followingUserId);

        if(!isFollowing) throw new BadRequestException("Current user is not following "+ followingUserId+" so cannot unfollow");
        log.info("Current user is following {}", followingUserId);

        personRepository.unfollow(currentUserId, followingUserId);
        log.info("Successfully unfollowed {}", followingUserId);

    }

    @Override
    public List<PersonDto> getMyFollowers() {

        Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Current user is: {}",currentUserId);

        List<Person> followers= personRepository.getFollowers(currentUserId);

        log.info("Fetching all the followers! ");
        return followers.stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toList();
    }

    @Override
    public List<PersonDto> getMyFollowing() {

        Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Current user is: {}",currentUserId);

        List<Person> following= personRepository.getFollowing(currentUserId);

        log.info("Fetching all the following list! ");
        return following.stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toList();
    }

    @Override
    public void removeFollower(Long followerUserId) {

        Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Current user is: {}",currentUserId);

        Boolean isFollowing = personRepository.isFollowing(followerUserId, currentUserId);

        if(!isFollowing) throw new BadRequestException("User is not following you!");
        log.info("Removing follower with userId: {}",followerUserId);

        personRepository.removeFollower(currentUserId,followerUserId);
        log.info("Successfully removed the follower!");
    }

}
