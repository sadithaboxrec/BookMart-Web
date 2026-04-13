package com.sadi.bookshop.controller;

import com.sadi.bookshop.dto.UserRequest;
import com.sadi.bookshop.dto.UserResponse;
import com.sadi.bookshop.exception.DuplicateResourceException;
import com.sadi.bookshop.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<?> registerProfile(
//         @Valid @RequestBody UserRequest userRequest) {
//
//        if(userService.hasCustomerWithEmail(userRequest.getEmail())){
////            return new ResponseEntity<>("Customer Already exists with that email" , HttpStatus.NOT_ACCEPTABLE);
//            throw new DuplicateResourceException("Email address already in use");
//        }
//
//        UserResponse response = userService.register(userRequest);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//    }


    // Public — always creates Role.USER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {

        if (userService.hasCustomerWithEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email address already in use");
        }

        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Protected — only existing SELLER can access (enforced by SecurityConfig)
    @PostMapping("/admin/register-seller")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody UserRequest request) {

        if (userService.hasCustomerWithEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email address already in use");
        }

        UserResponse response = userService.registerSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping("/activateProfile")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {

        boolean isActivated = userService.activateProfile(token);
        if (isActivated) {
            return ResponseEntity.ok("Profile activated ");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found");
        }
    }








    @GetMapping("/test")
    public String test() {
        return "test Successful";
    }

}
