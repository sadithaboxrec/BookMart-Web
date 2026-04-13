package com.sadi.bookshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Price cannot be empty")
    private Double price;

    private Double offerPrice;

    private String categoryId;
    private int quantity;


}