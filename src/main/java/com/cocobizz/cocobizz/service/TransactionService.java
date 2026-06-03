package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.TransactionDto;
import com.cocobizz.cocobizz.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getTransactionsByFarmer(Long farmerId);

    TransactionDto getTransactionById(Long id);

    TransactionDto updateTransaction(Long id, TransactionDto dto);

    void deleteTransaction(Long id);

    List<TransactionDto> getTransactionsByBuyer(Long userId);

    List<TransactionDto> getTransactionsByAdmin(Long adminId);


}