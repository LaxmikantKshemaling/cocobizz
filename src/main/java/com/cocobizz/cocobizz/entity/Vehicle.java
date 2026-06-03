package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleNumber;

    private String driverName;

    private String driverPhone;

    private BigDecimal capacity;

    private String currentLocation;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @OneToMany(mappedBy="vehicle", fetch = FetchType.LAZY)
    private List<Shipment> shipments;

}