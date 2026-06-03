package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.DeliveryChallanDto;
import com.cocobizz.cocobizz.dto.StockSimpleDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryChallanServiceImpl implements DeliveryChallanService {

    private final DeliveryChallanRepository dcRepo;
    private final StockInflowRepository stockRepo;
    private final WarehouseRepository warehouseRepo;
    private final ShipmentRepository shipmentRepo;

    private final BarcodeService barcodeService;
    private final PdfService pdfService;

    @Override
    @Transactional
    public DeliveryChallan createDC(List<Long> stockIds) {

        List<StockInflow> stocks = stockRepo.findAllById(stockIds);

        if (stocks.isEmpty()) {
            throw new RuntimeException("No stock found");
        }

        String dcNumber = "DC-" + System.currentTimeMillis();

        String barcodePath = barcodeService.generateBarcode(dcNumber);

        DeliveryChallan dc = DeliveryChallan.builder()
                .dcNumber(dcNumber)
                .barcodeValue(dcNumber)
                .barcodeImagePath(barcodePath)
                .farmerId(stocks.get(0).getFarmer().getUserId())
                .warehouseId(stocks.get(0).getWarehouse().getId())
                .stockList(stocks)
                .scanned(false)
                .build();

        DeliveryChallan saved = dcRepo.save(dc);

        // ✅ GENERATE PDF
        String pdfPath = pdfService.generateDC(saved);

        // ✅ IMPORTANT: SAVE AGAIN
        saved.setPdfPath(pdfPath);
        return dcRepo.save(saved);  // 🔥 FIX
    }


    // 🔥 MAIN SCAN API
    @Override
    @Transactional
    public DeliveryChallanDto scanAndReturnDC(String barcode) {

        DeliveryChallan dc = dcRepo.findByBarcodeValue(barcode)
                .orElseThrow(() -> new RuntimeException("❌ Invalid DC"));

        String message;

        // ✅ CHECK DELIVERY COMPLETE
        for (StockInflow stock : dc.getStockList()) {
            if (stock.getStatus() != StockStatus.DELIVERED) {
                throw new RuntimeException("❌ Delivery not completed yet");
            }
        }

        // ✅ ALREADY SCANNED
        if (Boolean.TRUE.equals(dc.getScanned())) {
            message = "⚠ Already Scanned";
        } else {

            // ✅ UPDATE WAREHOUSE STOCK
            for (StockInflow stock : dc.getStockList()) {

                Warehouse w = stock.getWarehouse();

                BigDecimal qty = stock.getQuantity() != null
                        ? stock.getQuantity()
                        : BigDecimal.ZERO;

                BigDecimal current = w.getCurrentStock() != null
                        ? w.getCurrentStock()
                        : BigDecimal.ZERO;

                w.setCurrentStock(current.add(qty));
                warehouseRepo.save(w);
            }

            dc.setScanned(true);
            dcRepo.save(dc);

            message = "✅ Scanned Successfully";
        }

        DeliveryChallanDto dto = convertToDto(dc);
        dto.setScanMessage(message); // 🔥 IMPORTANT

        return dto;
    }

    @Override
    public Optional<DeliveryChallan> findByBarcodeValue(String barcode) {
        return dcRepo.findByBarcodeValue(barcode); // ✅ FIXED
    }

    // 🔥 DTO CONVERTER
    private DeliveryChallanDto convertToDto(DeliveryChallan dc) {

        if (dc.getStockList() == null || dc.getStockList().isEmpty()) {
            return DeliveryChallanDto.builder()
                    .id(dc.getId())
                    .dcNumber(dc.getDcNumber())
                    .barcodeValue(dc.getBarcodeValue())
                    .barcodeImagePath(dc.getBarcodeImagePath())
                    .pdfPath(dc.getPdfPath())
                    .scanned(dc.getScanned())

                    // ✅ ADD THIS (IMPORTANT FIX)
                    .createdDate(dc.getCreatedDate())

                    .stockList(List.of())
                    .build();
        }

        StockInflow stock = dc.getStockList().get(0);

        Users farmer = stock.getFarmer();
        Warehouse w = stock.getWarehouse();

        List<Shipment> shipments =
                shipmentRepo.findByStockInflow_Id(stock.getId());

        Shipment sh = shipments.isEmpty() ? null : shipments.get(0);

        return DeliveryChallanDto.builder()
                .id(dc.getId())
                .dcNumber(dc.getDcNumber())
                .barcodeValue(dc.getBarcodeValue())
                .barcodeImagePath(dc.getBarcodeImagePath())
                .pdfPath(dc.getPdfPath())
                .scanned(dc.getScanned())

                // ✅🔥 MAIN FIX (THIS WAS MISSING)
                .createdDate(dc.getCreatedDate())

                .farmerName(farmer != null ? farmer.getUserName() : "N/A")
                .farmerPhone(farmer != null ? farmer.getPhone() : "N/A")
                .farmerAddress(farmer != null ? farmer.getAddress() : "N/A")

                .warehouseName(w != null ? w.getName() : "N/A")
                .warehouseLocation(w != null ? w.getLocation() : "N/A")

                .shipmentStatus(sh != null ? sh.getStatus().name() : "NOT_ASSIGNED")
                .vehicleNumber(sh != null && sh.getVehicle() != null
                        ? sh.getVehicle().getVehicleNumber()
                        : "N/A")
                .driverName(sh != null && sh.getVehicle() != null
                        ? sh.getVehicle().getDriverName()
                        : "N/A")

                .stockList(
                        dc.getStockList().stream().map(s ->
                                StockSimpleDto.builder()
                                        .productName(s.getProductName())
                                        .quantity(
                                                s.getQuantity() != null
                                                        ? s.getQuantity().doubleValue()
                                                        : 0
                                        )
                                        .build()
                        ).toList()
                )
                .build();
    }





    @Override
    public List<DeliveryChallanDto> getByFarmerId(Long farmerId) {

        return dcRepo.findByFarmerId(farmerId).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public List<DeliveryChallanDto> getAllDC() {

        return dcRepo.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }
}