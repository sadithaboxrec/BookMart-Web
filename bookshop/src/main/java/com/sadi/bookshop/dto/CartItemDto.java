package com.sadi.bookshop.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private String productId;
    private String name;
    private Double price;
    private String image;
    private Integer quantity;

}