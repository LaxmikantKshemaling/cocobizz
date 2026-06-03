package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "shipments", uniqueConstraints = {
        @UniqueConstraint(columnNames = "stock_inflow_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stock_inflow_id", nullable = false)
    private StockInflow stockInflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distributor_id")
    private Users distributor;

    private String startLocation;
    private String destinationLocation;

    private LocalDateTime departureTime;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;



    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void onCreate(){
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedDate = LocalDateTime.now();
    }

    private Double currentLat;
    private Double currentLng;



}