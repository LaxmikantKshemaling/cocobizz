package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    // ✅ CATEGORY DETAILS
    private Long categoryId;
    private String categoryName;
    private String categoryImage;

    // ✅ PRODUCT DETAILS
    private BigDecimal unitPrice;
    private String unitType;
    private String description;

    // ✅ PRODUCT IMAGE (FULL URL PATH FROM BACKEND)
    private String imageUrl;
}