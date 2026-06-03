package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.WarehouseDto;
import com.cocobizz.cocobizz.entity.Warehouse;
import com.cocobizz.cocobizz.service.WarehouseService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class WarehouseController {

    private final WarehouseService warehouseService;

    // CREATE WAREHOUSE
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody WarehouseDto dto) {

        Warehouse warehouse = warehouseService.createWarehouse(dto);

        return ResponseEntity.status(201).body(warehouse);
    }

    // GET ALL WAREHOUSES
    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {

        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    // GET WAREHOUSE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {

        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    // UPDATE WAREHOUSE
    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(
            @PathVariable Long id,
            @RequestBody WarehouseDto dto
    ) {

        return ResponseEntity.ok(warehouseService.updateWarehouse(id, dto));
    }

    // DELETE WAREHOUSE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable Long id) {

        warehouseService.deleteWarehouse(id);

        return ResponseEntity.ok("Warehouse deleted successfully");
    }
}