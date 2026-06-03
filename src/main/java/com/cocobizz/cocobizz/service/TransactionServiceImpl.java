package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.TransactionDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repo;

    @Override
    public List<TransactionDto> getAllTransactions() {
        return repo.findAll().stream().map(this::convert).toList();
    }

    @Override
    public List<TransactionDto> getTransactionsByFarmer(Long farmerId) {
        return repo.findByFarmer_UserId(farmerId)
                .stream().map(this::convert).toList();
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return convert(repo.findById(id).orElseThrow());
    }

    @Override
    public TransactionDto updateTransaction(Long id, TransactionDto dto) {

        Transaction t = repo.findById(id).orElseThrow();

        t.setAmount(dto.getAmount());
        t.setPaymentMode(PaymentMode.valueOf(dto.getPaymentMode()));

        return convert(repo.save(t));
    }

    @Override
    public void deleteTransaction(Long id) {
        repo.deleteById(id);
    }


    @Override
    public List<TransactionDto> getTransactionsByAdmin(Long adminId) {
        return repo.findByPurchaser_UserId(adminId)
                .stream()
                .map(this::convert)
                .toList();
    }




    @Override
    public List<TransactionDto> getTransactionsByBuyer(Long userId) {
        return repo.findByPurchaser_UserId(userId)
                .stream()
                .map(this::convert)
                .toList();
    }

    private TransactionDto convert(Transaction t) {

        return TransactionDto.builder()
                .id(t.getId())
                .transactionId(t.getTransactionId())
                .purchaserName(
                        t.getPurchaser() != null ? t.getPurchaser().getUserName() : null
                )
                .farmerName(
                        t.getFarmer() != null ? t.getFarmer().getUserName() : "N/A"
                )
                .amount(t.getAmount())
                .paymentMode(t.getPaymentMode().name())
                .transactionType(t.getTransactionType().name())
                .transactionDate(t.getTransactionDate())
                .build();
    }
}