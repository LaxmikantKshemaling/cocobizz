package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.StockOutflowDto;
import com.cocobizz.cocobizz.entity.StockOutflow;
import com.cocobizz.cocobizz.repository.StockOutflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockOutflowServiceImpl implements StockOutflowService {

    private final StockOutflowRepository stockOutflowRepository;

    @Override
    public List<StockOutflowDto> getAllOutflows() {

        return stockOutflowRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public StockOutflowDto getOutflowById(Long id) {

        StockOutflow outflow = stockOutflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock outflow not found"));

        return convertToDto(outflow);
    }

    private StockOutflowDto convertToDto(StockOutflow outflow) {

        return StockOutflowDto.builder()
                .id(outflow.getId())
                .productId(outflow.getProduct().getId())
                .productName(outflow.getProduct().getName())
                .unitType(outflow.getProduct().getUnitType())
                .warehouseId(outflow.getWarehouse().getId())
                .warehouseName(outflow.getWarehouse().getName())
                .customerId(outflow.getCustomer().getUserId())
                .customerName(outflow.getCustomer().getUserName())
                .quantity(outflow.getQuantity())
                .pricePerUnit(outflow.getPricePerUnit())
                .totalAmount(outflow.getTotalAmount())
                .outflowDate(outflow.getOutflowDate())
                .status(outflow.getStatus())
                .build();
    }
}