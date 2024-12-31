package com.microBuzz.post_service.service;

import com.microBuzz.post_service.dto.PostCreateRequestDto;
import com.microBuzz.post_service.dto.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostCreateRequestDto postDto, Long userId);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsOfUser(Long userId);
}
