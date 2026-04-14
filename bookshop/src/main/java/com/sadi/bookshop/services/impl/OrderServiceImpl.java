package com.sadi.bookshop.services.impl;

import com.razorpay.RazorpayClient;
import com.sadi.bookshop.dto.CheckoutRequest;
import com.sadi.bookshop.dto.CheckoutResponse;
import com.sadi.bookshop.dto.OrderResponse;
import com.sadi.bookshop.dto.PaymentVerifyRequest;
import com.sadi.bookshop.entity.*;
import com.sadi.bookshop.enums.OrderStatus;
import com.sadi.bookshop.exception.ResourceNotFoundException;
import com.sadi.bookshop.repo.CartRepo;
import com.sadi.bookshop.repo.OrderRepo;
import com.sadi.bookshop.repo.ProductRepo;
import com.sadi.bookshop.services.OrderService;
import com.sadi.bookshop.services.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    final RazorpayService razorpayService;



    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String razorpaySecret;



//    @Override
//    public OrderResponse checkout(String userId, CheckoutRequest request) {
//
//        // 1. GET CART
//        Cart cart = cartRepo.findByUserId(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
//
//        if (cart.getItems() == null || cart.getItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//
//        // 2. VALIDATE STOCK
//        for (CartItem item : cart.getItems()) {
//
//            Product product = productRepo.findById(item.getProductId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//            if (product.getQuantity() < item.getQuantity()) {
//                throw new RuntimeException("Not enough stock for " + product.getName());
//            }
//        }
//
//        // 3. CREATE ORDER ITEMS (SNAPSHOT)
//        List<OrderItem> orderItems = cart.getItems().stream()
//                .map(i -> {
//                    OrderItem oi = new OrderItem();
//                    oi.setProductId(i.getProductId());
//                    oi.setName(i.getName());
//                    oi.setPrice(i.getPrice());
//                    oi.setQuantity(i.getQuantity());
//                    return oi;
//                })
//                .toList();
//
//        // 4. CALCULATE TOTAL
//        double total = orderItems.stream()
//                .mapToDouble(i -> i.getPrice() * i.getQuantity())
//                .sum();
//
//        // 5. CREATE ORDER
//        Order order = new Order();
//        order.setUserId(userId);
//        order.setItems(orderItems);
//        order.setAddress(request.getAddress());
//        order.setTotalAmount(total);
//        order.setStatus(OrderStatus.PENDING);
//        order.setCreatedAt(LocalDateTime.now());
//
//        Order savedOrder = orderRepo.save(order);
//
//        // 6. REDUCE STOCK
//        for (CartItem item : cart.getItems()) {
//
//            Product product = productRepo.findById(item.getProductId()).get();
//
//            product.setQuantity(product.getQuantity() - item.getQuantity());
//
//            // optional: auto disable
//            if (product.getQuantity() <= 0) {
//                product.setActive(false);
//            }
//
//            productRepo.save(product);
//        }
//
//        // 7. CLEAR CART
//        cart.getItems().clear();
//        cart.setUpdatedAt(LocalDateTime.now());
//        cartRepo.save(cart);
//
//        return mapToResponse(savedOrder);
//    }
//

    // =================================================================================
    // updating checokout so it can be use with order status and payement checking

//    public Map<String, Object> checkout(String userId, CheckoutRequest request) throws Exception {
//
//        Cart cart = cartRepo.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        if (cart.getItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//
//        // validate + create orderItems
//
//          List<OrderItem> orderItems = cart.getItems().stream()
//                .map(i -> {
//                    OrderItem oi = new OrderItem();
//                    oi.setProductId(i.getProductId());
//                    oi.setName(i.getName());
//                    oi.setPrice(i.getPrice());
//                    oi.setQuantity(i.getQuantity());
//                    return oi;
//                })
//                .toList();
//
//        double total = cart.getItems().stream()
//                .mapToDouble(i -> i.getPrice() * i.getQuantity())
//                .sum();
//
//        Order order = new Order();
//        order.setUserId(userId);
////        order.setItems(...); // same mapping
//        order.setItems(orderItems);
//        order.setAddress(request.getAddress());
//        order.setTotalAmount(total);
//        order.setStatus(OrderStatus.PENDING);
//        order.setCreatedAt(LocalDateTime.now());
//
//
//
//
//        order = orderRepo.save(order);
//
//        //  CREATE RAZORPAY ORDER
//        RazorpayClient client = razorpayService.getClient();
//
//        JSONObject options = new JSONObject();
//        options.put("amount", (int)(total * 100)); // paise
//        options.put("currency", "LKR");
//        options.put("receipt", order.getId());
//
//        com.razorpay.Order razorOrder = client.orders.create(options);
//
//        order.setRazorpayOrderId(razorOrder.get("id"));
//        orderRepo.save(order);
//
//        return Map.of(
//                "orderId", order.getId(),
//                "razorpayOrderId", razorOrder.get("id"),
//                "amount", total,
//                "key", key
//        );
//    }




@Override
public CheckoutResponse checkout(String userId, CheckoutRequest request) {

    Cart cart = cartRepo.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

    if (cart.getItems().isEmpty()) {
        throw new RuntimeException("Cart is empty");
    }

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

    double total = orderItems.stream()
            .mapToDouble(i -> i.getPrice() * i.getQuantity())
            .sum();

    Order order = Order.builder()
            .userId(userId)
            .items(orderItems)
            .address(request.getAddress())
            .totalAmount(total)
            .status(OrderStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();

    order = orderRepo.save(order);

    try {
        RazorpayClient client = razorpayService.getClient();

        JSONObject options = new JSONObject();
        options.put("amount", (int)(total * 100));
        options.put("currency", "INR");
        options.put("receipt", order.getId());

        com.razorpay.Order razorOrder = client.orders.create(options);

        order.setRazorpayOrderId(razorOrder.get("id"));
        orderRepo.save(order);

        return CheckoutResponse.builder()
                .orderId(order.getId())
                .razorpayOrderId(razorOrder.get("id"))
                .amount(total)
                .currency("INR")
                .build();

    } catch (Exception e) {
        throw new RuntimeException("Razorpay error: " + e.getMessage());
    }
}


    // =============================================
    // payement verification and stock reduction

    public void verifyPayment(PaymentVerifyRequest request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();

        String generatedSignature = hmacSHA256(data, razorpaySecret);

        if (!generatedSignature.equals(request.getRazorpaySignature())) {
            throw new RuntimeException("Invalid payment signature");
        }

        order.setStatus(OrderStatus.PAID);
        order.setRazorpayPaymentId(request.getRazorpayPaymentId());

        orderRepo.save(order);

        // 🔥 STOCK REDUCTION AFTER PAYMENT ONLY
        for (OrderItem item : order.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setQuantity(product.getQuantity() - item.getQuantity());

            if (product.getQuantity() <= 0) {
                product.setActive(false);
            }

            productRepo.save(product);
        }

        // clear cart
        cartRepo.findByUserId(order.getUserId()).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepo.save(cart);
        });
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


   // =======================================================================
    // utility method to support payement verification

    private String hmacSHA256(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKey =
                    new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");

            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Signature error");
        }
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