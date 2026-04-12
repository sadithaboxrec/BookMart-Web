package com.sadi.bookshop.repo;

import com.sadi.bookshop.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationToken(String activationToken);

    Optional<User> findFirstByEmail(String email);

}
