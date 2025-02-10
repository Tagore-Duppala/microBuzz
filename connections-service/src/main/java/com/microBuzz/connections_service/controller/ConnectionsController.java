package com.microBuzz.connections_service.controller;

import com.microBuzz.connections_service.dto.NewUserDto;
import com.microBuzz.connections_service.dto.PersonDto;
import com.microBuzz.connections_service.entity.Person;
import com.microBuzz.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getMyFirstConnections(){
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections());
    }

    @PostMapping("/newuser")
    public ResponseEntity<Void> createNewUser(@RequestBody NewUserDto newUserDto){
        connectionsService.createNewUser(newUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/follow/{followUserId}")
    public ResponseEntity<Void> followUser(@PathVariable Long followUserId){
        connectionsService.followUser(followUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unfollow/{followUserId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followUserId){
        connectionsService.unfollowUser(followUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("followers")
    public ResponseEntity<List<PersonDto>> getMyFollowers(){
        return ResponseEntity.ok(connectionsService.getMyFollowers());
    }

    @GetMapping("following")
    public ResponseEntity<List<PersonDto>> getMyFollowing(){
        return ResponseEntity.ok(connectionsService.getMyFollowing());
    }

    @PostMapping("/remove-follower/{followerUserId}")
    public ResponseEntity<Void> removeFollower(@PathVariable Long followerUserId){
        connectionsService.removeFollower(followerUserId);
        return ResponseEntity.noContent().build();
    }
}
