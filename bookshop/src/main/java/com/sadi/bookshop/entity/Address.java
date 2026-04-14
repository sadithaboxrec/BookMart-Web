package com.sadi.bookshop.entity;

import lombok.Data;

@Data
public class Address {

    private String fullName;
    private String phone;
    private String street;
    private String city;
    private String district;
    private String postalCode;
}