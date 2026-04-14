package com.sadi.bookshop.dto;

import com.sadi.bookshop.entity.Address;
import com.sadi.bookshop.entity.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {

    private String orderId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;

    private Address address;
}