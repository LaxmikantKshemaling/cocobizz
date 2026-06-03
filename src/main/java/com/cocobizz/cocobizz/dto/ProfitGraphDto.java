package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitGraphDto {

    private String label;
    private BigDecimal profit;
    private BigDecimal revenue; // 🔥 ADD THIS
}