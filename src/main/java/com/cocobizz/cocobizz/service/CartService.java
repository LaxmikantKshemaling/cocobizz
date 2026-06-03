package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.CartDto;

public interface CartService {

    CartDto getCart(Long userId);

    CartDto addToCart(Long userId, Long productId, Integer qty);

    CartDto updateQuantity(Long userId, Long productId, Integer qty);

    CartDto removeItem(Long userId, Long productId);

    void clearCart(Long userId);
}
