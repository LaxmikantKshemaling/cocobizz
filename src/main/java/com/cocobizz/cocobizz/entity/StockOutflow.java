package com.cocobizz.cocobizz.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_outflow")
public class StockOutflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // PRODUCT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    // WAREHOUSE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;


    // CUSTOMER / BUYER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Users customer;

    private BigDecimal quantity;

    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "outflow_date")
    private LocalDate outflowDate;

    private String status;



}
