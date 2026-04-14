package com.sadi.bookshop.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;

public class RazorPaySignatureTest {

    public static void main(String[] args) throws Exception {



        String razorpayOrderId = "order_SdMx2LBC00QbQ9";
        String razorpayPaymentId = "pay_test_123456";

        String secret = "get_it from _application_properties";

        String payload = razorpayOrderId + "|" + razorpayPaymentId;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());

        String signature = HexFormat.of().formatHex(hash);

        System.out.println("SIGNATURE = " + signature);
    }
}