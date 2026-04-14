package com.sadi.bookshop.repo;

import com.sadi.bookshop.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepo extends MongoRepository<Cart, String> {

    Optional<Cart> findByUserId(String userId);
}
