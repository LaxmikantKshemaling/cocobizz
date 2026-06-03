package com.cocobizz.cocobizz.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;
    private Long userId;
    private BigDecimal totalAmount;
    private List<CartItemDto> items;


}
