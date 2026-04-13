package com.sadi.bookshop.controller;

import com.sadi.bookshop.dto.ProductRequest;
import com.sadi.bookshop.dto.ProductResponse;
import com.sadi.bookshop.services.ImageUploadService;
import com.sadi.bookshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;
    private final ImageUploadService imageUploadService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductResponse> create(
            @RequestPart("request") String request,
            @RequestPart("images") List<MultipartFile> images) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ProductRequest productRequest =
                mapper.readValue(request, ProductRequest.class);

        List<String> urls = imageUploadService.uploadImages(images);

        return ResponseEntity.ok(
                productService.createProduct(productRequest, urls)
        );
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductResponse> update(
            @PathVariable String id,
            @RequestPart("request") String request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ProductRequest productRequest =
                mapper.readValue(request, ProductRequest.class);

        List<String> urls = (images != null)
                ? imageUploadService.uploadImages(images)
                : null;

        return ResponseEntity.ok(
                productService.updateProduct(id, productRequest, urls)
        );
    }
}