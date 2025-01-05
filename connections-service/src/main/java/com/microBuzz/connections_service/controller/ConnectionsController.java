package com.microBuzz.connections_service.controller;

import com.microBuzz.connections_service.dto.PersonDto;
import com.microBuzz.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<PersonDto>> getMyFirstConnections(){
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections());
    }

    @GetMapping("/follow/{followUserId}")
    public ResponseEntity<Void> followUser(@PathVariable Long followUserId){
        connectionsService.followUser(followUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unfollow/{followUserId}")
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

    @GetMapping("/remove-follower/{followerUserId}")
    public ResponseEntity<Void> removeFollower(@PathVariable Long followerUserId){
        connectionsService.removeFollower(followerUserId);
        return ResponseEntity.noContent().build();
    }


}
