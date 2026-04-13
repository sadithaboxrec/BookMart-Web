package com.sadi.bookshop.services.impl;

import com.sadi.bookshop.dto.CategoryRequest;
import com.sadi.bookshop.dto.CategoryResponse;
import com.sadi.bookshop.dto.CategoryUpdateRequest;
import com.sadi.bookshop.entity.Category;
import com.sadi.bookshop.exception.ResourceNotFoundException;
import com.sadi.bookshop.repo.CategoryRepo;
import com.sadi.bookshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepo categoryRepo;

    public boolean hasCategory(String name) {
        return categoryRepo.findByName(name).isPresent();
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        categoryRepo.save(category);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }





    public List<CategoryResponse> findAllCategories() {

        List<Category> categories = categoryRepo.findAll();
        List<CategoryResponse> categoriesResponse = new ArrayList<>();

        for (Category category : categories) {

                    CategoryResponse response = CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .description(category.getDescription())
                            .build();

            categoriesResponse.add(response);

        }
        return categoriesResponse;

    }

    public CategoryResponse findCategoryById(String id) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();

    }





    public CategoryResponse updateCategory(String id, CategoryUpdateRequest updateRequest) {


        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee " + id + " not found"));

        if (updateRequest.getName() != null) {
            existing.setName(updateRequest.getName());
        }

        if (updateRequest.getDescription() != null) {
            existing.setDescription(updateRequest.getDescription());
        }


        Category updated = categoryRepo.save(existing);

        return CategoryResponse.builder()
                .id(updated.getId())
                .name(updated.getName())
                .description(updated.getDescription())
                .build();
    }


}
