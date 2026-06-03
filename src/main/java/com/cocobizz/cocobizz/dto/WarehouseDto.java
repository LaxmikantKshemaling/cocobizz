package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDto {

    private String name;
    private String location;
    private BigDecimal capacity;
    private BigDecimal currentStock;
}