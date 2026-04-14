package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.CheckoutRequest;
import com.sadi.bookshop.dto.CheckoutResponse;
import com.sadi.bookshop.dto.OrderResponse;
import com.sadi.bookshop.dto.PaymentVerifyRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {

    // order
//    OrderResponse checkout(String userId, CheckoutRequest request);

//    Map<String, Object> checkout(String userId, CheckoutRequest request) throws Exception;
    CheckoutResponse checkout(String userId, CheckoutRequest request);

    // user
    List<OrderResponse> getMyOrders(String userId);

    // seller
    List<OrderResponse> getAllOrders();

    void verifyPayment(PaymentVerifyRequest request);

}
