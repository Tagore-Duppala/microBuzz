package com.microBuzz.post_service.service.Impl;

import com.microBuzz.post_service.auth.UserContextHolder;
import com.microBuzz.post_service.entity.Post;
import com.microBuzz.post_service.entity.PostLike;
import com.microBuzz.post_service.event.PostLikedEvent;
import com.microBuzz.post_service.exception.BadRequestException;
import com.microBuzz.post_service.exception.ResourceNotFoundException;
import com.microBuzz.post_service.repository.PostLikeRepository;
import com.microBuzz.post_service.repository.PostRepository;
import com.microBuzz.post_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    @Override
    public void likePost(Long postId) {
        log.info("Liking post with post id {} is in process", postId);

        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+ postId));

        Boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(isLiked) throw new BadRequestException("Post is already liked, Cannot like again");

        PostLike postLike = new PostLike();

        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);
        log.info("Post is liked successfully!");

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();
        log.info("post liked event created successfully!!");

        kafkaTemplate.send("post-liked-topic",postId,postLikedEvent);
        log.info("post-liked-topic message sent, {}", postLikedEvent);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) {
        log.info("Unliking post wiht post id {} is in process", postId);

        Long userId = Long.valueOf(UserContextHolder.getCurrentUserId());

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+ postId));

        Boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!isLiked) throw new BadRequestException("Post is not liked, Cannot unlike");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);

        log.info("post with id {} unliked successfully",postId);
    }
}
