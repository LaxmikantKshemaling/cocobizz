package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeDto {

    private Long id;

    private String barcodeValue;

    private String type; // WAREHOUSE / PRODUCT

    private Long stockInflowId;

    private Long productId;

    private Boolean scanned;

    private LocalDateTime generatedAt;
}