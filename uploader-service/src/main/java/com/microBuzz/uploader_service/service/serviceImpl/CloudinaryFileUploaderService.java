package com.microBuzz.uploader_service.service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.microBuzz.uploader_service.service.FileUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryFileUploaderService implements FileUploaderService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) {

        try{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            log.error("Error occurred while uploading image: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
