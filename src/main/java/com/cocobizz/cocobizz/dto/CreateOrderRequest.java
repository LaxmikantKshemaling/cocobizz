package com.cocobizz.cocobizz.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String address;
    private String paymentMethod;
}