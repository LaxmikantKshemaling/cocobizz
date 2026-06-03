package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.WarehouseDto;
import com.cocobizz.cocobizz.entity.Warehouse;
import com.cocobizz.cocobizz.repository.WarehouseRepository;
import com.cocobizz.cocobizz.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public Warehouse createWarehouse(WarehouseDto dto) {

        Warehouse warehouse = Warehouse.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .capacity(dto.getCapacity())
                .currentStock(dto.getCurrentStock())
                .build();

        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
    }

    @Override
    public Warehouse updateWarehouse(Long id, WarehouseDto dto) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        warehouse.setName(dto.getName());
        warehouse.setLocation(dto.getLocation());
        warehouse.setCapacity(dto.getCapacity());
        warehouse.setCurrentStock(dto.getCurrentStock());

        return warehouseRepository.save(warehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        warehouseRepository.delete(warehouse);
    }
}