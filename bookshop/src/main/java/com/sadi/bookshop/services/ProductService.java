package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.ProductRequest;
import com.sadi.bookshop.dto.ProductResponse;
import com.sadi.bookshop.dto.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request, List<String> imageUrls);

//    ProductResponse updateProduct(String id, ProductRequest request, List<String> imageUrls);

    ProductResponse updateProduct(String id, ProductUpdateRequest request, List<String> imageUrls);

    ProductResponse getProductById(String id);

    List<ProductResponse> getAllProducts();

    void reduceStock(String productId, int quantity);

}