package com.sadi.bookshop.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    List<String> uploadImages(List<MultipartFile> files);

    void deleteImage(String imageUrl);
}