package com.cocobizz.cocobizz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinanceDto {

    private BigDecimal revenue;
    private BigDecimal purchase;
    private BigDecimal profit;
    private BigDecimal loss;
}
