package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.TransactionDto;
import com.cocobizz.cocobizz.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDto> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<TransactionDto> getTransactionsByFarmer(@PathVariable Long farmerId){
        return transactionService.getTransactionsByFarmer(farmerId);
    }


    @GetMapping("/admin/{adminId}")
    public List<TransactionDto> getTransactionsByAdmin(@PathVariable Long adminId){
        return transactionService.getTransactionsByAdmin(adminId);
    }


    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable Long id){
        return transactionService.getTransactionById(id);
    }

    @PutMapping("/{id}")
    public TransactionDto updateTransaction(@PathVariable Long id,
                                            @RequestBody TransactionDto dto){
        return transactionService.updateTransaction(id,dto);
    }


    @GetMapping("/buyer/{userId}")
    public List<TransactionDto> getBuyerTransactions(@PathVariable Long userId){
        return transactionService.getTransactionsByBuyer(userId);
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return "Transaction deleted successfully";
    }
}