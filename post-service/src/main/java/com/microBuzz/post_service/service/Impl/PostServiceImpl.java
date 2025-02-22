package com.microBuzz.post_service.service.Impl;

import com.microBuzz.post_service.auth.UserContextHolder;
import com.microBuzz.post_service.clients.ConnectionsClient;
import com.microBuzz.post_service.dto.PersonDto;
import com.microBuzz.post_service.dto.PostCreateRequestDto;
import com.microBuzz.post_service.dto.PostDto;
import com.microBuzz.post_service.entity.Post;
import com.microBuzz.post_service.event.PostCreatedEvent;
import com.microBuzz.post_service.repository.PostRepository;
import com.microBuzz.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postDto) {

        try {
            Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Creating the post for user with user id: {}", userId);
            Post post = modelMapper.map(postDto, Post.class);
            post.setUserId(userId);

            Post savedPost = postRepository.save(post);

            PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                    .postId(savedPost.getId())
                    .creatorId(savedPost.getUserId())
                    .content(savedPost.getContent())
                    .build();

            kafkaTemplate.send("post-created-topic", postCreatedEvent);

            return modelMapper.map(savedPost, PostDto.class);
        }
        catch (Exception ex) {
            log.error("Exception occurred in createPost , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in createPost: "+ex.getMessage());
        }
    }

    public PostDto getPostById(Long postId) {

        try {
            Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Retrieving post with ID: {}", postId);
            log.info("Current user is: {}", userId);
            Post post = postRepository.findById(postId).orElseThrow(() ->
                    new RuntimeException("Post not found with id: " + postId));
            return modelMapper.map(post, PostDto.class);
        } catch (Exception ex) {
            log.error("Exception occurred in getPostById , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getPostById: "+ex.getMessage());
        }
    }

    public List<PostDto> getAllPostsOfUser() {

        try {
            Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());
            log.info("Current user is: {}", userId);
            List<Post> posts = postRepository.findByUserId(userId);

            List<PersonDto> connections = connectionsClient.getMyFirstConnections();

            return posts
                    .stream()
                    .map((element) -> modelMapper.map(element, PostDto.class))
                    .collect(Collectors.toList());
        }
        catch (Exception ex) {
            log.error("Exception occurred in getAllPostsOfUser , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in getAllPostsOfUser: "+ex.getMessage());
        }
    }
}
