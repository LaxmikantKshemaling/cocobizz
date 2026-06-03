package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private Long productId;
    private String productName;
    private String categoryName;

    private String warehouseName;
    private String location;

    private BigDecimal purchasedQty;
    private BigDecimal purchasedPrice;
    private String purchasedUnitType;

    private BigDecimal soldQty;
    private BigDecimal soldPrice;
    private String soldUnitType;

    private BigDecimal currentStock;
    private String currentUnitType;

    private String status;

    // ✅ NEW FIELDS
    private BigDecimal costPerUnit;
    private BigDecimal inventoryValue;
    private BigDecimal profit;
}