package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.InventoryDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final StockInflowRepository inflowRepo;
    private final OrderItemRepository orderItemRepo;

    @Override
    public List<InventoryDto> getInventory() {

        List<StockInflow> inflows = inflowRepo.findAll();
        List<OrderItems> allItems = orderItemRepo.findAll();

        Map<String, InventoryDto> map = new HashMap<>();

        // ================= PURCHASE GROUP =================
        for (StockInflow inflow : inflows) {

            if (!Boolean.TRUE.equals(inflow.getPaymentDone())) continue;

            String key = inflow.getProductName().toLowerCase().trim();

            map.putIfAbsent(key, InventoryDto.builder()
                    .productId(inflow.getProduct() != null ? inflow.getProduct().getId() : null)
                    .productName(inflow.getProductName())
                    .categoryName(inflow.getCategoryName())
                    .warehouseName(inflow.getWarehouse().getName())
                    .location(inflow.getWarehouse().getLocation())
                    .purchasedQty(BigDecimal.ZERO)
                    .purchasedPrice(BigDecimal.ZERO)
                    .soldQty(BigDecimal.ZERO)
                    .soldPrice(BigDecimal.ZERO)
                    .build());

            InventoryDto dto = map.get(key);

            dto.setPurchasedQty(dto.getPurchasedQty().add(
                    inflow.getQuantity() != null ? inflow.getQuantity() : BigDecimal.ZERO));

            dto.setPurchasedPrice(dto.getPurchasedPrice().add(
                    inflow.getTotalAmount() != null ? inflow.getTotalAmount() : BigDecimal.ZERO));
        }

        // ================= SALES GROUP =================
        for (OrderItems item : allItems) {

            if (item.getProduct() == null) continue;
            if (item.getOrder() == null ||
                    !"SUCCESS".equalsIgnoreCase(item.getOrder().getPaymentStatus())) continue;

            String key = item.getProduct().getName().toLowerCase().trim();

            InventoryDto dto = map.get(key);
            if (dto == null) continue;

            dto.setSoldQty(dto.getSoldQty().add(BigDecimal.valueOf(item.getQuantity())));

            dto.setSoldPrice(dto.getSoldPrice().add(
                    item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO));
        }

        // ================= FINAL CALCULATION =================
        List<InventoryDto> result = new ArrayList<>();

        for (InventoryDto dto : map.values()) {

            BigDecimal purchasedQty = dto.getPurchasedQty();
            BigDecimal soldQty = dto.getSoldQty();

            BigDecimal currentStock = purchasedQty.subtract(soldQty);

            // ✅ COST PER UNIT
            BigDecimal costPerUnit = BigDecimal.ZERO;

            if (purchasedQty.compareTo(BigDecimal.ZERO) > 0) {
                costPerUnit = dto.getPurchasedPrice()
                        .divide(purchasedQty, 2, BigDecimal.ROUND_HALF_UP);
            }

            // ✅ COST OF SOLD
            BigDecimal costOfSold = costPerUnit.multiply(soldQty);

            // ✅ PROFIT
            BigDecimal profit = dto.getSoldPrice().subtract(costOfSold);

            // ✅ INVENTORY VALUE
            BigDecimal inventoryValue = costPerUnit.multiply(currentStock);

            // ✅ STATUS
            String status;
            if (currentStock.compareTo(BigDecimal.ZERO) <= 0) {
                status = "OUT_OF_STOCK";
            } else if (currentStock.compareTo(new BigDecimal("10")) < 0) {
                status = "LOW_STOCK";
            } else {
                status = "IN_STOCK";
            }

            dto.setCurrentStock(currentStock);
            dto.setStatus(status);

            dto.setCostPerUnit(costPerUnit);
            dto.setInventoryValue(inventoryValue);
            dto.setProfit(profit);

            dto.setCurrentUnitType("kg");
            dto.setPurchasedUnitType("kg");
            dto.setSoldUnitType("kg");

            result.add(dto);
        }

        return result;
    }
}