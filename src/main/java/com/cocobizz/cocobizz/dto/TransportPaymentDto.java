package com.cocobizz.cocobizz.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransportPaymentDto {

    // Shipment reference
    private Long shipmentId;

    // Vehicle used
    private Long vehicleId;

    // Driver details
    private String driverName;
    private String driverPhone;

    // Who receives payment
    private Long transporterId;

    private Long adminId;// optional if transporter company

    // Payment details
    private BigDecimal amount;

    private String paymentMode;

    // Who paid (Admin)
    private Long paidBy;

    // Notes
    private String description;

}