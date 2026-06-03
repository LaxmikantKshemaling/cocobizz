package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.WarehouseDto;
import com.cocobizz.cocobizz.entity.Warehouse;

import java.util.List;

public interface WarehouseService {

    Warehouse createWarehouse(WarehouseDto dto);

    List<Warehouse> getAllWarehouses();

    Warehouse getWarehouseById(Long id);

    Warehouse updateWarehouse(Long  id, WarehouseDto dto);

    void deleteWarehouse(Long id);
}