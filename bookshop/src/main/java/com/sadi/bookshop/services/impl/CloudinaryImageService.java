package com.sadi.bookshop.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sadi.bookshop.services.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageService implements ImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public List<String> uploadImages(List<MultipartFile> files) {

        return files.stream().map(file -> {
            try {

                Map uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "bookshop/products",
                                "resource_type", "image"
                        )
                );

                return uploadResult.get("secure_url").toString();

            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }).toList();
    }

// remove image when need to update it

    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image from Cloudinary");
        }
    }


    private String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String fileName = parts[parts.length - 1];

        String publicIdWithFolder = parts[parts.length - 2] + "/" + fileName;

        return publicIdWithFolder.substring(0, publicIdWithFolder.lastIndexOf("."));
    }

}