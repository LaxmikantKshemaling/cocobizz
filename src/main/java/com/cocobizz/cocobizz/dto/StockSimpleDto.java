package com.cocobizz.cocobizz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockSimpleDto {
    private String productName;
    private Double quantity;
}
