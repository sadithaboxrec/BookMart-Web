package com.sadi.bookshop.services.impl;

import com.sadi.bookshop.dto.UserRequest;
import com.sadi.bookshop.dto.UserResponse;
import com.sadi.bookshop.entity.User;
import com.sadi.bookshop.enums.Role;
import com.sadi.bookshop.repo.UserRepo;
import com.sadi.bookshop.services.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EmailServiceImpl emailServiceImpl;

    @Lazy
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;




    @Override
    public UserResponse register(UserRequest request,Role role) {

        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
//        user.setPassword(request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
//        user.setCreatedAt(request.getCreatedAt());
//        user.setUpdatedAt(request.getUpdatedAt());
        user.setRole(role);
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
                .role(saved.getRole().name())
                .build();

    }





//    public User validateAndGetUser(String email, String password) {
//
//        // 1. Let Spring Security validate credentials (throws AuthenticationException on failure)
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(email, password)
//        );
//
//        // 2. Account activation check
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        if (!Boolean.TRUE.equals(user.getIsActive())) {
//            throw new DisabledException("Account is not activated. Please check your email.");
//        }
//
//        return user;
//    }

    public User validateAndGetUser(String email, String password) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new DisabledException("Account is not activated");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        return user;
    }



    @Override
    public UserResponse register(UserRequest request) {
        return register(request, Role.USER);
    }

    public UserResponse registerSeller(UserRequest request) {
        return register(request, Role.SELLER);
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
