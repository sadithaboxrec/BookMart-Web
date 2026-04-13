package com.sadi.bookshop.controller;

import com.sadi.bookshop.dto.ProductResponse;
import com.sadi.bookshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return productService.getProductById(id);
    }
}