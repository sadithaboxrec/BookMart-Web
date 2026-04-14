package com.sadi.bookshop.dto;

import lombok.Data;

@Data
public class AddToCartRequest {

    private String productId;
    private Integer quantity;
}