package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;




@Builder
@Getter
@Setter
public class TransactionDto {

    private Long id;

    private String transactionId;

    private Long purchaserId;
    private String purchaserName;

    private Long farmerId;
    private String farmerName;

    private BigDecimal amount;

    private String transactionType;

    private String paymentMode;

    private String description;

    private LocalDateTime transactionDate;
}