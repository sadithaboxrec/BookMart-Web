package com.sadi.bookshop.entity;

import com.sadi.bookshop.enums.OrderStatus;
import com.sadi.bookshop.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;

    private List<OrderItem> items;

    private Address address;

    private double totalAmount;

    private OrderStatus status; // in enum

    private LocalDateTime createdAt;
}