package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    // ✅ BEST PRACTICE (no filter in stream)
    List<Orders> findByCustomer_UserId(Long userId);
}