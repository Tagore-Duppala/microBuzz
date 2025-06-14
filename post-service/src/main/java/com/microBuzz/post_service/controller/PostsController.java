package com.microBuzz.post_service.controller;

import com.microBuzz.post_service.dto.PostCreateRequestDto;
import com.microBuzz.post_service.dto.PostDto;
import com.microBuzz.post_service.service.PostService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;

    @PostMapping("/createPost")
    public ResponseEntity<PostDto> createPost(@RequestPart @NonNull PostCreateRequestDto postDto, @RequestPart MultipartFile image) {
        PostDto createdPost = postService.createPost(postDto, image);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser() {
        List<PostDto> posts = postService.getAllPostsOfUser();
        return ResponseEntity.ok(posts);
    }

}
