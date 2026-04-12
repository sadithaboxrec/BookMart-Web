package com.sadi.bookshop.services.impl;

import com.sadi.bookshop.dto.UserRequest;
import com.sadi.bookshop.dto.UserResponse;
import com.sadi.bookshop.entity.User;
import com.sadi.bookshop.repo.UserRepo;
import com.sadi.bookshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EmailServiceImpl emailServiceImpl;

    @Override
    public UserResponse register(UserRequest request) {

        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
//        user.setCreatedAt(request.getCreatedAt());
//        user.setUpdatedAt(request.getUpdatedAt());
        user.setCartItems(new HashMap<>());

        user.setActivationToken(UUID.randomUUID().toString());

        User saved=userRepo.save(user);

        // activation email for token
        String activationLink="http://localhost:8080/api/v1.0/activateProfile?token="+user.getActivationToken();
        String subject = "Activating Your Stationary Mart account for Purchase";
        String body = "Click on the  link to activate your account: " + activationLink;
        emailServiceImpl.sendEmail(user.getEmail(), subject, body);


        return  UserResponse.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .name(saved.getName())
                .build();

    }




    public boolean hasCustomerWithEmail(String email) {

        return userRepo.findFirstByEmail(email).isPresent();
    }


    public boolean activateProfile(String activationToken) {

        return userRepo.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    userRepo.save(profile);
                    return true;
                })
                .orElse(false);
    }


    public boolean isAccountActive(String email) {

        return userRepo.findByEmail(email)
                .map(User::getIsActive)
                .orElse(false);
    }

}
