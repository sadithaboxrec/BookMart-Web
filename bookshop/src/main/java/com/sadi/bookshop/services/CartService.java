package com.sadi.bookshop.services;

import com.sadi.bookshop.dto.AddToCartRequest;
import com.sadi.bookshop.dto.CartResponse;

public interface CartService {

//    CartResponse addToCart(String cartId, AddToCartRequest request);

    CartResponse addToCart(String cartId, String userId, AddToCartRequest request);

//    CartResponse getCart(String cartId);

    // after login getting cart id aand user id for controller methods
    CartResponse getCart(String cartId, String userId);


//    CartResponse removeFromCart(String cartId, String productId);
    CartResponse removeFromCart(String cartId, String userId, String productId);


//    CartResponse decreaseQty(String cartId, String productId);
    CartResponse decreaseQty(String cartId, String userId, String productId);

//    CartResponse increaseQty(String cartId, String productId);

    CartResponse increaseQty(String cartId, String userId, String productId);

//    CartResponse clearCart(String cartId);
    public CartResponse clearCart(String cartId, String userId);

    void mergeCart(String guestCartId, String userId);


;
}