package com.sadi.bookshop.services.impl;

import com.sadi.bookshop.dto.CheckoutRequest;
import com.sadi.bookshop.dto.OrderResponse;
import com.sadi.bookshop.entity.*;
import com.sadi.bookshop.enums.OrderStatus;
import com.sadi.bookshop.exception.ResourceNotFoundException;
import com.sadi.bookshop.repo.CartRepo;
import com.sadi.bookshop.repo.OrderRepo;
import com.sadi.bookshop.repo.ProductRepo;
import com.sadi.bookshop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;


    @Override
    public OrderResponse checkout(String userId, CheckoutRequest request) {

        // 1. GET CART
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. VALIDATE STOCK
        for (CartItem item : cart.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + product.getName());
            }
        }

        // 3. CREATE ORDER ITEMS (SNAPSHOT)
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(i -> {
                    OrderItem oi = new OrderItem();
                    oi.setProductId(i.getProductId());
                    oi.setName(i.getName());
                    oi.setPrice(i.getPrice());
                    oi.setQuantity(i.getQuantity());
                    return oi;
                })
                .toList();

        // 4. CALCULATE TOTAL
        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        // 5. CREATE ORDER
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setAddress(request.getAddress());
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepo.save(order);

        // 6. REDUCE STOCK 🔥
        for (CartItem item : cart.getItems()) {

            Product product = productRepo.findById(item.getProductId()).get();

            product.setQuantity(product.getQuantity() - item.getQuantity());

            // optional: auto disable
            if (product.getQuantity() <= 0) {
                product.setActive(false);
            }

            productRepo.save(product);
        }

        // 7. CLEAR CART
        cart.getItems().clear();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepo.save(cart);

        return mapToResponse(savedOrder);
    }



    @Override
    public List<OrderResponse> getMyOrders(String userId) {
        return orderRepo.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .orderId(order.getId())
                .items(order.getItems())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .address(order.getAddress())
                .build();
    }
}