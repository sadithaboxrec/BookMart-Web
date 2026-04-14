package com.sadi.bookshop.dto;

import com.sadi.bookshop.entity.Address;
import lombok.Data;

@Data
public class CheckoutRequest {
    private Address address;
}