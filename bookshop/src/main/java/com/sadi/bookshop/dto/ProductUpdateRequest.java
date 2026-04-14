package com.sadi.bookshop.dto;

import java.util.*;
import lombok.Data;


@Data
public class ProductUpdateRequest {

    private String name;
    private String description;
    private Double price;
    private Double offerPrice;
    private String categoryId;
    private Integer quantity;

    // to remove images
    private List<String> removeImages;

}