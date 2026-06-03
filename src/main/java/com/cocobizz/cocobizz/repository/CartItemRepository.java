package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
