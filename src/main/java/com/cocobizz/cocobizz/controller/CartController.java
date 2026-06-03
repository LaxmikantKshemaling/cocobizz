package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.CartDto;
import com.cocobizz.cocobizz.security.SecurityUtil;
import com.cocobizz.cocobizz.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService service;
    private final SecurityUtil securityUtil;

    // GET CART
    @GetMapping
    public CartDto getCart(HttpServletRequest request){
        Long userId = securityUtil.getCurrentUserId(request);
        return service.getCart(userId);
    }

    // ADD
    @PostMapping("/add")
    public CartDto add(
            @RequestParam Long productId,
            @RequestParam Integer qty,
            HttpServletRequest request){

        Long userId = securityUtil.getCurrentUserId(request);

        return service.addToCart(userId, productId, qty);
    }

    // UPDATE
    @PutMapping("/update")
    public CartDto update(
            @RequestParam Long productId,
            @RequestParam Integer qty,
            HttpServletRequest request){

        Long userId = securityUtil.getCurrentUserId(request);

        return service.updateQuantity(userId, productId, qty);
    }

    // REMOVE
    @DeleteMapping("/remove/{productId}")
    public CartDto remove(
            @PathVariable Long productId,
            HttpServletRequest request){

        Long userId = securityUtil.getCurrentUserId(request);

        return service.removeItem(userId, productId);
    }

    // CLEAR
    @DeleteMapping("/clear")
    public String clear(HttpServletRequest request){

        Long userId = securityUtil.getCurrentUserId(request);

        service.clearCart(userId);

        return "Cart cleared";
    }
}