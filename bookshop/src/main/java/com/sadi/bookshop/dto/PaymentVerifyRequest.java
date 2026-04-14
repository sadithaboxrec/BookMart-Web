package com.sadi.bookshop.dto;

import lombok.Data;

@Data
public class PaymentVerifyRequest {

    private String orderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}