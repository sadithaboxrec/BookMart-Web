package com.sadi.bookshop.controller;


import com.sadi.bookshop.dto.AddToCartRequest;
import com.sadi.bookshop.dto.CartResponse;
import com.sadi.bookshop.services.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Get or create cartId cookie so user can add to cart items without login
    private String getOrCreateCartId(HttpServletRequest request, HttpServletResponse response) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("cartId")) {
                    return cookie.getValue();
                }
            }
        }

        String cartId = UUID.randomUUID().toString();

        Cookie cookie = new Cookie("cartId", cartId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 10); // for 10 days

        response.addCookie(cookie);

        return cartId;
    }

    @PostMapping("/add")
    public CartResponse addToCart(
            @RequestBody AddToCartRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        String cartId = getOrCreateCartId(httpRequest, httpResponse);
        return cartService.addToCart(cartId, request);
    }

    @GetMapping
    public CartResponse getCart(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        String cartId = getOrCreateCartId(httpRequest, httpResponse);
        return cartService.getCart(cartId);
    }




// remove from cart
    @DeleteMapping("/remove/{productId}")
    public CartResponse removeFromCart(
            @PathVariable String productId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String cartId = getOrCreateCartId(request, response);
        return cartService.removeFromCart(cartId, productId);
    }



    @PatchMapping("/increase/{productId}")
    public CartResponse increase(@PathVariable String productId,
                                 HttpServletRequest req,
                                 HttpServletResponse res) {
        return cartService.increaseQty(getOrCreateCartId(req, res), productId);
    }

    @PatchMapping("/decrease/{productId}")
    public CartResponse decrease(@PathVariable String productId,
                                 HttpServletRequest req,
                                 HttpServletResponse res) {
        return cartService.decreaseQty(getOrCreateCartId(req, res), productId);
    }



    @DeleteMapping("/clear")
    public CartResponse clear(HttpServletRequest req,
                              HttpServletResponse res) {
        return cartService.clearCart(getOrCreateCartId(req, res));
    }



}