package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class PaymentDto {

    private Long id;
    private String paymentId;
    private Long stockInflowId;

    // FARMER
    private String farmerName;
    private String farmerEmail;
    private String farmerPhone;
    private String farmerAddress;

    // ADMIN
    private String purchaserName;
    private String purchaserEmail;
    private String purchaserPhone;

    // PRODUCT
    private String productName;
    private String categoryName;
    private String unitType;
    private Double quantity;

    // WAREHOUSE
    private String warehouseName;

    // PAYMENT
    private Double totalAmount;
    private Double amount;
    private String status;
    private String paymentGateway;
    private LocalDateTime createdAt;
}