package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "delivery_challan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryChallan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dcNumber;

    @Column(unique = true)
    private String barcodeValue;

    private Long farmerId;

    private Long warehouseId;

    private Boolean scanned = false;

    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "dc_id")
    private List<StockInflow> stockList;

    private String barcodeImagePath;
    private String pdfPath;


    @PrePersist
    public void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
