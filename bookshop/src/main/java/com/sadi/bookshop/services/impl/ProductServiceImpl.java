package com.sadi.bookshop.services.impl;

import com.sadi.bookshop.dto.ProductRequest;
import com.sadi.bookshop.dto.ProductResponse;
import com.sadi.bookshop.dto.ProductUpdateRequest;
import com.sadi.bookshop.entity.Category;
import com.sadi.bookshop.entity.Product;
import com.sadi.bookshop.exception.ResourceNotFoundException;
import com.sadi.bookshop.repo.CategoryRepo;
import com.sadi.bookshop.repo.ProductRepo;
import com.sadi.bookshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    private final CloudinaryImageService imageUploadService;

    @Override
    public ProductResponse createProduct(ProductRequest request, List<String> imageUrls) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .offerPrice(request.getOfferPrice())
                .categoryId(request.getCategoryId())
                .quantity(request.getQuantity())
                .images(imageUrls)
                .active(request.getQuantity() > 0)
                .createdAt(LocalDateTime.now())
                .build();

        Product saved = productRepo.save(product);

        updateCategoryStatus(request.getCategoryId());

        return mapToResponse(saved);
    }

//    @Override
//    public ProductResponse updateProduct(String id, ProductRequest request, List<String> imageUrls) {
//
//        Product product = productRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setOfferPrice(request.getOfferPrice());
//        product.setCategoryId(request.getCategoryId());
//        product.setQuantity(request.getQuantity());
//
//        if (imageUrls != null && !imageUrls.isEmpty()) {
//            product.setImages(imageUrls);
//        }
//
//        product.setActive(request.getQuantity() > 0);
//        product.setUpdatedAt(LocalDateTime.now());
//
//        Product saved = productRepo.save(product);
//
//        updateCategoryStatus(product.getCategoryId());
//
//        return mapToResponse(saved);
//    }

    @Override
    public ProductResponse updateProduct(String id, ProductUpdateRequest request, List<String> imageUrls) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getOfferPrice() != null) {
            product.setOfferPrice(request.getOfferPrice());
        }

        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }

        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
            product.setActive(request.getQuantity() > 0);
        }

        //  Remove images (Cloudinary first, then DB)
        if (request.getRemoveImages() != null && !request.getRemoveImages().isEmpty()) {

            List<String> existingImages = product.getImages();

            if (existingImages != null) {

                for (String imageUrl : request.getRemoveImages()) {

                    if (existingImages.contains(imageUrl)) {

                        try {
                            // Delete from Cloudinary FIRST
                            imageUploadService.deleteImage(imageUrl);

                            // 2. Then remove from DB list
                            existingImages.remove(imageUrl);

                        } catch (Exception e) {
                            // Important: don't silently ignore
                            throw new RuntimeException("Failed to delete image: " + imageUrl);
                        }
                    }
                }
            }
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<String> existingImages = product.getImages();
            if (existingImages != null) {
                existingImages.addAll(imageUrls);
            } else {
                product.setImages(imageUrls);
            }
        }

        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepo.save(product);

        updateCategoryStatus(product.getCategoryId());

        return mapToResponse(saved);
    }



    @Override
    public ProductResponse getProductById(String id) {
        return mapToResponse(productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void reduceStock(String productId, int qty) {

        Product product = productRepo.findById(productId)
                .orElseThrow();

        int newQty = product.getQuantity() - qty;

        product.setQuantity(newQty);
        product.setActive(newQty > 0);

        productRepo.save(product);

        updateCategoryStatus(product.getCategoryId());
    }

    private void updateCategoryStatus(String categoryId) {

        boolean hasStock = productRepo
                .existsByCategoryIdAndQuantityGreaterThan(categoryId, 0);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow();

        category.setActive(hasStock);

        categoryRepo.save(category);
    }

    private ProductResponse mapToResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .offerPrice(p.getOfferPrice())
                .images(p.getImages())
                .categoryId(p.getCategoryId())
                .quantity(p.getQuantity())
                .active(p.isActive())
                .build();
    }
}



//private void updateCategoryStatusSafely(String categoryId) {
//    if (categoryId == null) return;
//
//    boolean hasStock = productRepo
//            .existsByCategoryIdAndQuantityGreaterThan(categoryId, 0);
//
//    categoryRepo.findById(categoryId).ifPresent(category -> {
//        category.setActive(hasStock);
//        categoryRepo.save(category);
//    });
//}