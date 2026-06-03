package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private LocalDateTime orderDate;
    private String address;

    private List<OrderItemDto> items;
}