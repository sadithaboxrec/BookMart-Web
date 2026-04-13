package com.sadi.bookshop.services;


import com.sadi.bookshop.dto.CategoryRequest;
import com.sadi.bookshop.dto.CategoryResponse;
import com.sadi.bookshop.dto.CategoryUpdateRequest;

import java.util.List;

public interface CategoryService{

    boolean hasCategory(String name);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> findAllCategories();

    CategoryResponse updateCategory(String id, CategoryUpdateRequest updateRequest);

    CategoryResponse findCategoryById(String id);

}
