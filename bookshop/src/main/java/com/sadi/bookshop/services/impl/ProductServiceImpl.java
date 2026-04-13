package com.sadi.bookshop.services.impl;

import com.sadi.bookshop.dto.ProductRequest;
import com.sadi.bookshop.dto.ProductResponse;
import com.sadi.bookshop.entity.Category;
import com.sadi.bookshop.entity.Product;
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

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request, List<String> imageUrls) {

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOfferPrice(request.getOfferPrice());
        product.setCategoryId(request.getCategoryId());
        product.setQuantity(request.getQuantity());

        if (imageUrls != null && !imageUrls.isEmpty()) {
            product.setImages(imageUrls);
        }

        product.setActive(request.getQuantity() > 0);
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