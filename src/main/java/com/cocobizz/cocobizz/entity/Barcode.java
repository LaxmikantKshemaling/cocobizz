package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "barcodes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Barcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String barcodeValue;

    private String type; // WAREHOUSE / PRODUCT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_inflow_id")
    private StockInflow stockInflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String barcodeImagePath; // 🔥 ADD THIS

    private Boolean scanned = false;

    private LocalDateTime generatedAt;

    @PrePersist
    public void onCreate(){
        this.generatedAt = LocalDateTime.now();
        if(this.scanned == null){
            this.scanned = false;
        }
    }
}