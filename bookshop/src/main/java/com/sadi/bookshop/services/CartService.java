package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.AddToCartRequest;
import com.sadi.bookshop.dto.CartResponse;

public interface CartService {

    CartResponse addToCart(String cartId, AddToCartRequest request);

    CartResponse getCart(String cartId);


    CartResponse removeFromCart(String cartId, String productId);


    CartResponse decreaseQty(String cartId, String productId);

    CartResponse increaseQty(String cartId, String productId);

    CartResponse clearCart(String cartId);
}