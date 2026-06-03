package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.InventoryDto;
import com.cocobizz.cocobizz.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

    private final InventoryService service;

    @GetMapping
    public List<InventoryDto> getInventory(){
        return service.getInventory();
    }
}