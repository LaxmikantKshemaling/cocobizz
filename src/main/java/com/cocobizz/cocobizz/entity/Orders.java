package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Users customer;

    private BigDecimal totalAmount;

    private String status;

    private String paymentStatus;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    private String deliveryAddress;

    private Double deliveryLat;

    private Double deliveryLng;

    // ✅ IMPORTANT FIX (cascade + init list)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItems> items = new ArrayList<>();


    // 🔥 ADD THIS FIELD
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}