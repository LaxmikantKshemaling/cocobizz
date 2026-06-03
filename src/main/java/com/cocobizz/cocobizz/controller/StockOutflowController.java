package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.service.StockOutflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-outflow")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StockOutflowController {

    private final StockOutflowService stockOutflowService;

    @GetMapping
    public ResponseEntity<?> getAllOutflows() {

        return ResponseEntity.ok(stockOutflowService.getAllOutflows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOutflowById(@PathVariable Long id) {

        return ResponseEntity.ok(stockOutflowService.getOutflowById(id));
    }
}