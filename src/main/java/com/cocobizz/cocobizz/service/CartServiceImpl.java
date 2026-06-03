package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.CartDto;
import com.cocobizz.cocobizz.dto.CartItemDto;
import com.cocobizz.cocobizz.entity.Cart;
import com.cocobizz.cocobizz.entity.CartItem;
import com.cocobizz.cocobizz.entity.Product;
import com.cocobizz.cocobizz.entity.Users;
import com.cocobizz.cocobizz.repository.CartItemRepository;
import com.cocobizz.cocobizz.repository.CartRepository;
import com.cocobizz.cocobizz.repository.ProductRepository;
import com.cocobizz.cocobizz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // ================= GET CART =================
    @Override
    public CartDto getCart(Long userId) {

        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseGet(() -> createCart(userId));

        return map(cart);
    }

    // ================= ADD =================
    @Override
    public CartDto addToCart(Long userId, Long productId, Integer qty) {

        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseGet(() -> createCart(userId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if(item != null){
            item.setQuantity(item.getQuantity() + qty);
        } else {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(qty)
                    .unitPrice(product.getUnitPrice())
                    .build();

            cart.getItems().add(item);
        }

        item.setTotalPrice(item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())));

        recalc(cart);

        return map(cartRepo.save(cart));
    }

    // ================= UPDATE =================
    @Override
    public CartDto updateQuantity(Long userId, Long productId, Integer qty) {

        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(qty);

        item.setTotalPrice(item.getUnitPrice()
                .multiply(BigDecimal.valueOf(qty)));

        recalc(cart);

        return map(cartRepo.save(cart));
    }

    // ================= REMOVE =================
    @Override
    public CartDto removeItem(Long userId, Long productId) {

        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));

        recalc(cart);

        return map(cartRepo.save(cart));
    }

    // ================= CLEAR =================
    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepo.save(cart);
    }

    // ================= CREATE =================
    private Cart createCart(Long userId){

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = Cart.builder()
                .user(user)
                .items(new java.util.ArrayList<>()) // ✅ FIX
                .totalAmount(BigDecimal.ZERO)
                .build();

        return cartRepo.save(cart);
    }

    // ================= CALC =================
    private void recalc(Cart cart){

        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
    }

    // ================= DTO =================
    private CartDto map(Cart cart){

        return CartDto.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getUserId())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream().map(i ->
                        CartItemDto.builder()
                                .productId(i.getProduct().getId())
                                .productName(i.getProduct().getName())
                                .imageUrl(
                                        i.getProduct().getImageUrl() != null
                                                ? "/product-images/" + i.getProduct().getImageUrl()
                                                : null
                                )
                                .quantity(i.getQuantity())
                                .unitPrice(i.getUnitPrice())
                                .totalPrice(i.getTotalPrice())
                                .build()
                ).toList())
                .build();
    }
}