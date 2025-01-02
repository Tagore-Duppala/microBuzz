package com.microBuzz.post_service.service.Impl;

import com.microBuzz.post_service.auth.UserContextHolder;
import com.microBuzz.post_service.clients.ConnectionsClient;
import com.microBuzz.post_service.dto.PersonDto;
import com.microBuzz.post_service.dto.PostCreateRequestDto;
import com.microBuzz.post_service.dto.PostDto;
import com.microBuzz.post_service.entity.Post;
import com.microBuzz.post_service.repository.PostRepository;
import com.microBuzz.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    public PostDto createPost(PostCreateRequestDto postDto) {

        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Creating the post for user with user id: {}",userId);
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Retrieving post with ID: {}", postId);
        log.info("Current user is: {}", userId);
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new RuntimeException("Post not found with id: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser() {

        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
        log.info("Current user is: {}", userId);
        List<Post> posts = postRepository.findByUserId(userId);

        List<PersonDto> connections = connectionsClient.getMyFirstConnections();

        return posts
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
