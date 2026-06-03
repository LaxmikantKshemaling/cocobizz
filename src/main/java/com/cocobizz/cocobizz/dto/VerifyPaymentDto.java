package com.cocobizz.cocobizz.dto;

import lombok.Data;

@Data
public class VerifyPaymentDto {

    private String paymentId;
    private boolean success;

}