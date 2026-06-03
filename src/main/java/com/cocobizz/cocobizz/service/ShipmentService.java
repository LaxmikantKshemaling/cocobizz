package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.ShipmentDto;

import java.util.List;

public interface ShipmentService {

    ShipmentDto assignTransport(ShipmentDto dto);

    ShipmentDto acceptShipment(Long id);

    ShipmentDto updateShipmentStatus(Long shipmentId, String status);

    ShipmentDto updateLocation(Long shipmentId, Double lat, Double lng);

    List<ShipmentDto> getAllShipments();

    ShipmentDto getShipmentById(Long id);

    public ShipmentDto rejectShipment(Long id);
}