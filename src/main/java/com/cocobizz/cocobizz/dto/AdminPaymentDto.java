package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentDto {

    private String paymentId;
    private String buyerName;
    private String buyerEmail;

    private BigDecimal amount;
    private String status;
    private String paymentMode;

    private String orderNumber;
    private LocalDateTime createdAt;
}