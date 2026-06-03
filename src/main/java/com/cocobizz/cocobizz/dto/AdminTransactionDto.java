package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminTransactionDto {

    private String transactionId;

    private String buyerName;
    private String buyerEmail;

    private BigDecimal amount;
    private String type;
    private String paymentMode;

    private String description;
    private LocalDateTime date;
}