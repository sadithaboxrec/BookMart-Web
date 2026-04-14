package com.sadi.bookshop.repo;

import com.sadi.bookshop.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepo extends MongoRepository<Order, String> {

    List<Order> findByUserId(String userId);

}
