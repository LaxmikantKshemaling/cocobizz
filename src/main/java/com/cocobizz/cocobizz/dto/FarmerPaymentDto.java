package com.cocobizz.cocobizz.dto;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class FarmerPaymentDto {

    private Long stockInflowId;

    private Long purchaserId;

    private BigDecimal amount;

    private String paymentMode;
}