package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.UserRequest;
import com.sadi.bookshop.dto.UserResponse;
import com.sadi.bookshop.entity.User;
import com.sadi.bookshop.enums.Role;

public interface UserService {

//    UserResponse register(UserRequest request);
 UserResponse register(UserRequest request, Role role);

    boolean hasCustomerWithEmail(String email);

    boolean activateProfile(String activationToken);

    User validateAndGetUser(String email, String password);

    boolean isAccountActive(String email);

    UserResponse register(UserRequest request);

    UserResponse registerSeller(UserRequest request);
}
