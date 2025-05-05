package com.microBuzz.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostCreateRequestDto {

    private String content;
}
