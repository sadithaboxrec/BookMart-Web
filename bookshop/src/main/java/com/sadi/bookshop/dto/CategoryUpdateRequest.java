package com.sadi.bookshop.dto;

import lombok.Data;

@Data
public class CategoryUpdateRequest {

    private String name;
    private String description;
}
