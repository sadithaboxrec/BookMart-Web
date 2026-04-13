package com.sadi.bookshop.controller;

import com.sadi.bookshop.dto.CategoryRequest;
import com.sadi.bookshop.dto.CategoryResponse;
import com.sadi.bookshop.dto.CategoryUpdateRequest;
import com.sadi.bookshop.exception.DuplicateResourceException;
import com.sadi.bookshop.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) {

        if (categoryService.hasCategory(categoryRequest.getName())) {
            throw new DuplicateResourceException("Category already exists");
        }

        CategoryResponse response = categoryService.createCategory(categoryRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        List<CategoryResponse> categories=categoryService.findAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);

    }

@GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String id) {

        CategoryResponse response = categoryService.findCategoryById(id);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String id,
            @RequestBody CategoryUpdateRequest request) {

        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }


}
