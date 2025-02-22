package com.microBuzz.connections_service.service.Impl;

import com.microBuzz.connections_service.dto.NewUserDto;
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
    public List<Person> getFirstDegreeConnections(){

        try {
            Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Fetching first degree connections for user id {}", userId);
            return personRepository.getFirstDegreeConnections(userId);
        } catch (Exception ex) {
            log.error("Exception occurred in getFirstDegreeConnections , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getFirstDegreeConnections: "+ex.getMessage());
        }
    }

    @Override
    public void createNewUser(NewUserDto newUserDto) {

        try {
            log.info("Creating new user in connections db");
            Person person = modelMapper.map(newUserDto, Person.class);
            personRepository.createNewUser(person.getUserId(), person.getName(), person.getEmail(), person.getBio());
        } catch (Exception ex) {
            log.error("Exception occurred in createNewUser , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in createNewUser: "+ex.getMessage());
        }
    }

    @Override
    public void followUser(Long followUserId) {

        try {
            Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Current user is: {}", currentUserId);

            log.info("Checking the if user already follows");
            Boolean isFollowing = personRepository.isFollowing(currentUserId, followUserId);

            if (isFollowing) throw new BadRequestException("You are already following user: " + followUserId);
            personRepository.follow(currentUserId, followUserId);
            log.info("You are now following: {}", followUserId);
        } catch (Exception ex) {
            log.error("Exception occurred in followUser , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in followUser: "+ex.getMessage());
        }
    }

    @Override
    public void unfollowUser(Long followingUserId) {

        try {
            Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Current user is: {}", currentUserId);

            log.info("Checking the if Current user is following {}", followingUserId);
            Boolean isFollowing = personRepository.isFollowing(currentUserId, followingUserId);

            if (!isFollowing)
                throw new BadRequestException("Current user is not following " + followingUserId + " so cannot unfollow");
            log.info("Current user is following {}", followingUserId);

            personRepository.unfollow(currentUserId, followingUserId);
            log.info("Successfully unfollowed {}", followingUserId);
        } catch (Exception ex) {
            log.error("Exception occurred in unfollowUser , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in unfollowUser: "+ex.getMessage());
        }

    }

    @Override
    public List<PersonDto> getMyFollowers() {

        try {
            Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Current user is: {}", currentUserId);

            List<Person> followers = personRepository.getFollowers(currentUserId);

            log.info("Fetching all the followers! ");
            return followers.stream()
                    .map(person -> modelMapper.map(person, PersonDto.class))
                    .toList();
        } catch (Exception ex) {
            log.error("Exception occurred in getMyFollowers , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getMyFollowers: "+ex.getMessage());
        }
    }

    @Override
    public List<PersonDto> getMyFollowing() {

        try {
            Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Current user is: {}", currentUserId);

            List<Person> following = personRepository.getFollowing(currentUserId);

            log.info("Fetching all the following list! ");
            return following.stream()
                    .map(person -> modelMapper.map(person, PersonDto.class))
                    .toList();
        } catch (Exception ex) {
            log.error("Exception occurred in getMyFollowing , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getMyFollowing: "+ex.getMessage());
        }
    }

    @Override
    public void removeFollower(Long followerUserId) {

        try {
            Long currentUserId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Current user is: {}", currentUserId);

            Boolean isFollowing = personRepository.isFollowing(followerUserId, currentUserId);

            if (!isFollowing) throw new BadRequestException("User is not following you!");
            log.info("Removing follower with userId: {}", followerUserId);

            personRepository.removeFollower(currentUserId, followerUserId);
            log.info("Successfully removed the follower!");
        } catch (Exception ex) {
            log.error("Exception occurred in removeFollower , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in removeFollower: "+ex.getMessage());
        }
    }

}
