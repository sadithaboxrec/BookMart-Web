package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.CheckoutRequest;
import com.sadi.bookshop.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    // order
    OrderResponse checkout(String userId, CheckoutRequest request);

    // user
    List<OrderResponse> getMyOrders(String userId);

    // seller
    List<OrderResponse> getAllOrders();

}
