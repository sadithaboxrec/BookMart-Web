package com.sadi.bookshop.repo;

import com.sadi.bookshop.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepo extends MongoRepository<Category, String> {

     Optional <Category> findByName(String name);

}
