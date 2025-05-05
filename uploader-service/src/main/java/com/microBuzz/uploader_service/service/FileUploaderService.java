package com.microBuzz.uploader_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploaderService {

    String upload(MultipartFile file);
}
