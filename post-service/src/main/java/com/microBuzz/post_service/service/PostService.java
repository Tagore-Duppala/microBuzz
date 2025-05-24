package com.microBuzz.post_service.service;

import com.microBuzz.post_service.dto.PostCreateRequestDto;
import com.microBuzz.post_service.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostDto createPost(PostCreateRequestDto postDto, MultipartFile image);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsOfUser();
}
