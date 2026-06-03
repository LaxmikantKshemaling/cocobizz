package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_inflow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ PRODUCT RELATION (MANDATORY)



    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @Column(name = "product_name")
    private String productName;

    private String categoryName;
    private String unitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Users farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    private BigDecimal quantity;
    private BigDecimal totalAmount;

    @Column(name = "dc_id")
    private Long dcId;

    private LocalDate inflowDate;

    @Enumerated(EnumType.STRING)
    private StockStatus status;

    private Boolean paymentDone = false;
}