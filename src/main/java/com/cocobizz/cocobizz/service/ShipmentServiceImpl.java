package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.ShipmentDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepo;
    private final StockInflowRepository stockRepo;
    private final VehicleRepository vehicleRepo;
    private  final WarehouseRepository warehouseRepo;

    private final DeliveryChallanService dcService;

    // ================= ASSIGN =================
    @Override
    public ShipmentDto assignTransport(ShipmentDto dto) {

        if (dto.getStockInflowId() == null || dto.getVehicleId() == null) {
            throw new RuntimeException("Stock ID & Vehicle ID required");
        }

        StockInflow stock = stockRepo.findById(dto.getStockInflowId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Vehicle vehicle = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        List<Shipment> existing = shipmentRepo.findByStockInflow_Id(stock.getId());

        Shipment shipment;

        if (!existing.isEmpty()) {
            shipment = existing.get(0);
        } else {
            shipment = new Shipment();
            shipment.setTrackingId("TRK-" + UUID.randomUUID().toString().substring(0, 8));
            shipment.setStockInflow(stock);
            shipment.setDepartureTime(LocalDateTime.now());
        }

        shipment.setVehicle(vehicle);

        shipment.setStartLocation(
                stock.getFarmer() != null ? stock.getFarmer().getAddress() : "Unknown"
        );

        shipment.setDestinationLocation(
                stock.getWarehouse() != null ? stock.getWarehouse().getLocation() : "Unknown"
        );

        shipment.setStatus(ShipmentStatus.ASSIGNED);

        shipmentRepo.save(shipment);

        // ✅ ONLY UPDATE STATUS (NO STOCK ADD)
        stock.setStatus(StockStatus.IN_DELIVERY);
        stockRepo.save(stock);

        return convertToDto(shipment);
    }

    // ================= ACCEPT =================
    @Override
    public ShipmentDto acceptShipment(Long id) {
        Shipment s = shipmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        s.setStatus(ShipmentStatus.ACCEPTED);
        return convertToDto(shipmentRepo.save(s));
    }

    // ================= REJECT =================
    @Override
    public ShipmentDto rejectShipment(Long id) {
        Shipment s = shipmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        s.setStatus(ShipmentStatus.REJECTED);
        return convertToDto(shipmentRepo.save(s));
    }

    // ================= STATUS UPDATE =================
    @Override
    public ShipmentDto updateShipmentStatus(Long id, String status) {

        Shipment s = shipmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        ShipmentStatus newStatus = ShipmentStatus.valueOf(status);

        s.setStatus(newStatus);

        // ✅ AFTER DELIVERY
        if(newStatus == ShipmentStatus.DELIVERED){

            StockInflow stock = s.getStockInflow();

            // ✅ UPDATE STOCK STATUS
            stock.setStatus(StockStatus.DELIVERED);

            // ✅ GET WAREHOUSE
            Warehouse warehouse = stock.getWarehouse();

            // ✅ INCREASE CURRENT STOCK
            if(warehouse.getCurrentStock() == null){

                warehouse.setCurrentStock(stock.getQuantity());

            }else{

                warehouse.setCurrentStock(
                        warehouse.getCurrentStock()
                                .add(stock.getQuantity())
                );
            }

            // ✅ SAVE WAREHOUSE
            warehouseRepo.save(warehouse);

            // ✅ SAVE STOCK
            stockRepo.save(stock);

            // ✅ GENERATE DELIVERY CHALLAN
            dcService.createDC(List.of(stock.getId()));
        }

        return convertToDto(shipmentRepo.save(s));
    }

    // ================= LOCATION =================
    @Override
    public ShipmentDto updateLocation(Long shipmentId, Double lat, Double lng) {

        Shipment s = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        s.setCurrentLat(lat);
        s.setCurrentLng(lng);

        return convertToDto(shipmentRepo.save(s));
    }

    // ================= GET ALL =================
    @Override
    public List<ShipmentDto> getAllShipments() {
        return shipmentRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // ================= GET BY ID =================
    @Override
    public ShipmentDto getShipmentById(Long id) {
        Shipment s = shipmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        return convertToDto(s);
    }

    // ================= DTO =================
    private ShipmentDto convertToDto(Shipment s) {

        StockInflow stock = s.getStockInflow();
        Vehicle v = s.getVehicle();

        return ShipmentDto.builder()
                .id(s.getId())
                .trackingId(s.getTrackingId())

                .stockInflowId(stock != null ? stock.getId() : null)

                .vehicleId(v != null ? v.getId() : null)
                .vehicleNumber(v != null ? v.getVehicleNumber() : "N/A")

                .driverName(v != null ? v.getDriverName() : "N/A")
                .driverPhone(v != null ? v.getDriverPhone() : "N/A")
                .driverAddress(v != null ? v.getCurrentLocation() : "N/A")

                .status(s.getStatus() != null ? s.getStatus().name() : "ASSIGNED")

                .startLocation(s.getStartLocation())
                .destinationLocation(s.getDestinationLocation())

                .farmerName(stock != null && stock.getFarmer() != null
                        ? stock.getFarmer().getUserName() : "N/A")

                .farmerAddress(stock != null && stock.getFarmer() != null
                        ? stock.getFarmer().getAddress() : "N/A")

                .warehouseName(stock != null && stock.getWarehouse() != null
                        ? stock.getWarehouse().getName() : "N/A")

                .warehouseAddress(stock != null && stock.getWarehouse() != null
                        ? stock.getWarehouse().getLocation() : "N/A")

                .productName(stock != null ? stock.getProductName() : "N/A")
                .categoryName(stock != null ? stock.getCategoryName() : "N/A")
                .quantity(stock != null ? stock.getQuantity() : null)

                .createdDate(s.getCreatedDate())
                .updatedDate(s.getUpdatedDate())

                .currentLat(s.getCurrentLat())
                .currentLng(s.getCurrentLng())

                .build();
    }
}