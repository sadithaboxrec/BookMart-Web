package com.sadi.bookshop.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    // Map of productId -> CartItem
    private Map<String, CartItem> cartItems;

    private Boolean isActive=false;
    private String activationToken;

//    @CreationTimeStamp
    @CreatedDate
    private LocalDateTime createdAt;

//    @UpdatedTimeStamp
    @LastModifiedDate
    private LocalDateTime updatedAt;


//    @PrePersist
//    public void prePersist() {
//        if (this.isActive == null) {
//            isActive = false;
//        }
//    }

    @Data
    public static class CartItem {
        private Integer quantity;
    }

}
