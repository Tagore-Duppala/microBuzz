package com.microBuzz.uploader_service.controller;

import com.microBuzz.uploader_service.service.FileUploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploaderService fileUploaderService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file){
        String url = fileUploaderService.upload(file);
        return ResponseEntity.ok(url);
    }
}
