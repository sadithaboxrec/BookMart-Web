package com.sadi.bookshop.controller;

import com.sadi.bookshop.dto.LoginRequest;
import com.sadi.bookshop.entity.User;
import com.sadi.bookshop.exception.BadCredentialsException;
import com.sadi.bookshop.security.CookieService;
import com.sadi.bookshop.security.CustomUserDetails;
import com.sadi.bookshop.security.JwtService;
import com.sadi.bookshop.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        try {
            // Active check BEFORE hitting AuthenticationManager (saves a DB call on inactive users)
            if (!userService.isAccountActive(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Account not activated. Please check your email."));
            }



            User user = userService.validateAndGetUser(request.getEmail(), request.getPassword());

            System.out.println(user.getEmail());
            System.out.println(user.getRole());
            System.out.println(user.getIsActive());


            String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
            cookieService.addJwtCookie(response, token);

            // Return token + public profile (mirrors your reference code's response shape)
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "user", Map.of(
                            "id",    user.getId(),
                            "name",  user.getName(),
                            "email", user.getEmail(),
                            "role",  user.getRole().name()
                    )
            ));

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        cookieService.clearJwtCookie(response);
        return ResponseEntity.ok("Logged out");
    }


    @GetMapping("/is-auth")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "Not authorized");
            return ResponseEntity.status(401).body(body);
        }

        Map<String, Object> body = new HashMap<>();

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            body.put("email", user.getUsername());
            body.put("name", user.getName());
            body.put("role", user.getRole());
        }

        body.put("success", true);
        return ResponseEntity.ok(body);
    }





}