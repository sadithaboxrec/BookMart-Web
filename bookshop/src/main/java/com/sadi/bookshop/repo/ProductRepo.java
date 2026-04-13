package com.sadi.bookshop.repo;

import com.sadi.bookshop.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {

    List<Product> findByCategoryId(String categoryId);

    boolean existsByCategoryIdAndQuantityGreaterThan(String categoryId, int quantity);


}
