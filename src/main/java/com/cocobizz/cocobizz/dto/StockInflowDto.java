package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInflowDto {

    private Long id;

    // ❌ REMOVE REQUIRED PRODUCT ID
    private Long productId;

    // ✅ USE NAME FOR AUTO CREATE
    private String productName;

    private String categoryName;
    private String unitType;

    private Long warehouseId;
    private String warehouseName;

    private Long farmerId;
    private String farmerUserName;

    private String farmerPhone;
    private String farmerAddress;

    private BigDecimal quantity;
    private BigDecimal totalAmount;

    private LocalDate inflowDate;
    private String status;

    private Boolean paymentDone;
}