package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.UserRequest;
import com.sadi.bookshop.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRequest request);

    boolean hasCustomerWithEmail(String email);

    boolean activateProfile(String activationToken);
}
