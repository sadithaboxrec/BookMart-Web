package com.sadi.bookshop.repo;

import com.sadi.bookshop.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepo extends MongoRepository<Cart, String> {
}
