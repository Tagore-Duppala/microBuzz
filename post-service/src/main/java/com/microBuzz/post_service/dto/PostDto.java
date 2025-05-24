package com.microBuzz.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDto {

    private Long id;
    private String content;
    private String imageUrl;
    private Long userId;
    private LocalDateTime createdAt;


}
