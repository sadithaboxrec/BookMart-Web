package com.sadi.bookshop.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    private String productId;
    private String name;
    private Double price;
    private String image;
    private Integer quantity;
}