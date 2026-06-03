package com.cocobizz.cocobizz.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {

    private Long id;
    private String trackingId;

    private Long stockInflowId;

    private Long vehicleId;
    private String vehicleNumber;

    private String driverName;
    private String driverPhone;
    private String driverAddress;

    private Long distributorId;

    private String startLocation;
    private String destinationLocation;

    private String status;

    private String farmerName;
    private String farmerAddress;

    private String warehouseName;
    private String warehouseAddress;

    private String productName;
    private String categoryName;
    private BigDecimal quantity;

    // ✅ FIX HERE
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate; // 🔥 ADD THIS

    private Double currentLat;
    private Double currentLng;
}