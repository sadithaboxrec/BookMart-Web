package com.sadi.bookshop.services.impl;


import com.sadi.bookshop.dto.*;
import com.sadi.bookshop.entity.*;
import com.sadi.bookshop.exception.ResourceNotFoundException;
import com.sadi.bookshop.repo.*;
import com.sadi.bookshop.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final ProductRepo productRepo;

    @Override
    public CartResponse addToCart(String cartId, AddToCartRequest request) {

        Cart cart = cartRepo.findById(cartId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(cartId);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setItems(new ArrayList<>());
                    return newCart;
                });

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + request.getQuantity()
            );
        } else {
            CartItem item = CartItem.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getOfferPrice() != null ? product.getOfferPrice() : product.getPrice())
                    .image(product.getImages() != null && !product.getImages().isEmpty()
                            ? product.getImages().get(0)
                            : null)
                    .quantity(request.getQuantity())
                    .build();

            cart.getItems().add(item);
        }

        cart.setUpdatedAt(LocalDateTime.now());

        Cart saved = cartRepo.save(cart);

        return mapToResponse(saved);
    }

    @Override
    public CartResponse getCart(String cartId) {

        Cart cart = cartRepo.findById(cartId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(cartId);
                    newCart.setItems(new ArrayList<>());
                    return cartRepo.save(newCart);
                });

        return mapToResponse(cart);
    }




    // increse cart quantity

    public CartResponse increaseQty(String cartId, String productId) {

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }

        cart.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(cartRepo.save(cart));
    }

// decrease the quantity

    public CartResponse decreaseQty(String cartId, String productId) {

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> items = cart.getItems();

        for (int i = 0; i < items.size(); i++) {

            CartItem item = items.get(i);

            if (item.getProductId().equals(productId)) {

                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    items.remove(i);
                }
                break;
            }
        }

        cart.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(cartRepo.save(cart));
    }


// remove a product from item

    @Override
    public CartResponse removeFromCart(String cartId, String productId) {

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> items = cart.getItems();

        if (items != null) {
            items.removeIf(item -> item.getProductId().equals(productId));
        }

        cart.setUpdatedAt(LocalDateTime.now());

        Cart saved = cartRepo.save(cart);

        return mapToResponse(saved);
    }



    // clear entire cart

    public CartResponse clearCart(String cartId) {

        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cart.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(cartRepo.save(cart));
    }


    private CartResponse mapToResponse(Cart cart) {

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(
                        cart.getItems().stream().map(i ->
                                CartItemDto.builder()
                                        .productId(i.getProductId())
                                        .name(i.getName())
                                        .price(i.getPrice())
                                        .image(i.getImage())
                                        .quantity(i.getQuantity())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}