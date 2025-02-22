package com.microBuzz.post_service.service.Impl;

import com.microBuzz.post_service.auth.UserContextHolder;
import com.microBuzz.post_service.entity.Post;
import com.microBuzz.post_service.entity.PostLike;
import com.microBuzz.post_service.event.PostLikedEvent;
import com.microBuzz.post_service.exception.BadRequestException;
import com.microBuzz.post_service.exception.ResourceNotFoundException;
import com.microBuzz.post_service.repository.PostLikeRepository;
import com.microBuzz.post_service.repository.PostRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    private static Long postId = 1L;

    private static Long userId = 1L;

    private static Post post;

    private static PostLike postLike;

    @BeforeAll
    static void setup(){
        UserContextHolder.setCurrentUserId(userId);
        post = Post.builder()
                .id(1L)
                .content("first post")
                .userId(2L)
                .build();

        postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .id(1L)
                .build();
    }

    @AfterAll
    static void cleanup(){
        UserContextHolder.clear();

    }


    @Test
//    @Disabled
    void testUnlikePost_WhenPostFoundById_ThenReturnVoid(){

        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.existsByUserIdAndPostId(userId,postId)).thenReturn(true);
        doNothing().when(postLikeRepository).deleteByUserIdAndPostId(userId, postId);

        postLikeService.unlikePost(postId);

        verify(postRepository, times(1)).findById(postId);
        verify(postLikeRepository, times(1)).existsByUserIdAndPostId(1L, postId);
        verify(postLikeRepository, times(1)).deleteByUserIdAndPostId(1L, postId);

    }

    @Test
//    @Disabled
    void testUnlikePost_WhenPostNotFoundById_ThenThrowException(){

        UserContextHolder.setCurrentUserId(1L);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->{
            postLikeService.unlikePost(postId);
        });
    }

    @Test
//    @Disabled
    void testUnlikePost_WhenPostFoundByIdAndNotExistsByUserIdAndPostId_ThenThrowException(){


        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);

        assertThrows(BadRequestException.class, ()->{
            postLikeService.unlikePost(postId);
        });
    }

    @Test
    void testLikePost_WhenPostFoundByPostIdAndPersonExists_ThenReturnVoid(){

        when(postLikeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.save(any(PostLike.class))).thenReturn(postLike);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                        .likedByUserId(userId)
                        .postId(postId)
                        .creatorId(2L)
                        .build();

        CompletableFuture<SendResult<Long, PostLikedEvent>> completableFuture = CompletableFuture.completedFuture(
                new SendResult<>(new ProducerRecord<>("post-liked-topic", postId, postLikedEvent), null) // Mock SendResult
        );

        when(kafkaTemplate.send("post-liked-topic", postId, postLikedEvent)).thenReturn(completableFuture);

        postLikeService.likePost(postId);

        ArgumentCaptor<PostLike> postLikeArgumentCaptor = ArgumentCaptor.forClass(PostLike.class);

//        verify(postLikedEvent, times(1));
        verify(postRepository).findById(postId);
        verify(postLikeRepository).existsByUserIdAndPostId(userId, postId);
        verify(postLikeRepository).save(postLikeArgumentCaptor.capture());
        verify(kafkaTemplate).send("post-liked-topic", postId, postLikedEvent);

        PostLike capturedPostLike = postLikeArgumentCaptor.getValue();

        Assertions.assertThat(capturedPostLike.getPostId()).isEqualTo(postId);
        Assertions.assertThat(capturedPostLike.getUserId()).isEqualTo(userId);

    }

    @Test
    void testLikePost_WhenPostNotFoundByPostIdAndPersonExists_ThenThrowException(){

        when(postLikeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));


        Assertions.assertThatThrownBy(()-> postLikeService.likePost(postId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Post is already liked, Cannot like again");

        verify(postRepository).findById(postId);
        verify(postLikeRepository, never()).save(postLike);
        verify(postLikeRepository).existsByUserIdAndPostId(userId, postId);
    }

}