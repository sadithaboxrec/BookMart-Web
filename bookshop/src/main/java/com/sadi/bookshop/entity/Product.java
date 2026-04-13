package com.sadi.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    private String name;

    private String description;

    private Double price;

    private Double offerPrice;

    private List<String> images;

    private String categoryId;

    private int quantity;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}