package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Transaction;
import com.cocobizz.cocobizz.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFarmer_UserId(Long farmerId);
    List<Transaction> findByPurchaser_UserId(Long userId);

    List<Transaction> findByTransactionType(TransactionType type);
}