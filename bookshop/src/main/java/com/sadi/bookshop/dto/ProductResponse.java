package com.sadi.bookshop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private Double price;
    private Double offerPrice;
    private String categoryId;
    private int quantity;

    private boolean active;

    private List<String> images;
}