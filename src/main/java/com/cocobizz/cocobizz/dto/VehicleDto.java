package com.cocobizz.cocobizz.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private Long id;

    private String vehicleNumber;

    private String driverName;

    private String driverPhone;

    private BigDecimal capacity;

    private String currentLocation;

    private String status;

}