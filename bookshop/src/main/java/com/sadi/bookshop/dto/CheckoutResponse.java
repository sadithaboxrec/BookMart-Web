package com.sadi.bookshop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutResponse {

    private String orderId;
    private String razorpayOrderId;
    private double amount;
    private String currency;


}