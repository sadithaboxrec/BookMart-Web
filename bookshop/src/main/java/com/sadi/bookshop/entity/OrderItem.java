package com.sadi.bookshop.entity;

import lombok.Data;

@Data
public class OrderItem {

    private String productId;
    private String name;
    private double price;
    private int quantity;
}