package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {

    List<OrderItems> findByOrder_Id(Long orderId);
}