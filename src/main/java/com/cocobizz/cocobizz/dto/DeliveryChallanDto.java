package com.cocobizz.cocobizz.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DeliveryChallanDto {

    private Long id;
    private String dcNumber;
    private String barcodeValue;
    private String barcodeImagePath;
    private String pdfPath;
    private Boolean scanned;

    private String farmerName;
    private String farmerPhone;
    private String farmerAddress;

    private String warehouseName;
    private String warehouseLocation;

    private  String scanMessage;
    // 🔥 MUST ADD

    // 🔥 ADD THIS (MAIN FIX)
    private LocalDateTime createdDate;

    private String shipmentStatus;
    private String vehicleNumber;
    private String driverName;

    private List<StockSimpleDto> stockList;
}