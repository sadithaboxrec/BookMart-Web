package com.sadi.bookshop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class CategoryResponse {
    private String id;
    private String name;
    private String description;
    private boolean active;
}