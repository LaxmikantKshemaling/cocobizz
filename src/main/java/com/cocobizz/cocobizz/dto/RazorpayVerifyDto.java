package com.cocobizz.cocobizz.dto;

import lombok.Data;

@Data
public class RazorpayVerifyDto {

    private String razorpayOrderId;   // 🔥 from Razorpay
    private String paymentId;         // razorpay_payment_id
    private String signature;         // razorpay_signature

    private Long orderId;             // 🔥 YOUR DB ORDER ID
}