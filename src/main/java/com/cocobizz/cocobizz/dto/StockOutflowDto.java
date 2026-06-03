package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockOutflowDto {

    private Long id;

    private Long productId;

    private String productName;

    private String unitType;

    private Long warehouseId;

    private String warehouseName;

    private Long customerId;

    private String customerName;

    private BigDecimal quantity;

    private BigDecimal pricePerUnit;

    private BigDecimal totalAmount;

    private LocalDate outflowDate;

    private String status;
}