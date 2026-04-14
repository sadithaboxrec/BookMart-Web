package com.sadi.bookshop.controller;

import com.sadi.bookshop.dto.CheckoutRequest;
import com.sadi.bookshop.dto.OrderResponse;
import com.sadi.bookshop.security.CustomUserDetails;
import com.sadi.bookshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // CHECKOUT
    @PostMapping("/checkout")
    public OrderResponse checkout(
            @RequestBody CheckoutRequest request,
            Authentication authentication
    ) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return orderService.checkout(user.getId(), request);
    }

    //  USER ORDERS
    @GetMapping("/my")
    public List<OrderResponse> myOrders(Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return orderService.getMyOrders(user.getId());
    }

    //  SELLER VIEW
    @GetMapping("/admin")
    public List<OrderResponse> allOrders() {
        return orderService.getAllOrders();
    }
}