package com.microBuzz.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDto {

    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;


}
