package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Orders;
import com.cocobizz.cocobizz.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByFarmer_UserId(Long farmerId);

    List<Payment> findByPurchaser_UserId(Long userId);

    List<Payment> findByOrder(Orders order);

    List<Payment> findByPurchaserIsNotNull();

    Payment findByPaymentId(String paymentId);
}