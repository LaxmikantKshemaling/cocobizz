package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponseDto {

    // 📦 ORDER
    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;

    // 👤 BUYER
    private String buyerName;
    private String buyerEmail;
    private String address;

    // 💰 PAYMENT
    private String paymentStatus;
    private String paymentMode;

    // 💵 TOTAL
    private BigDecimal totalAmount;

    // 🛒 ITEMS
    private List<OrderItemDto> items;
}