package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.DeliveryChallanDto;
import com.cocobizz.cocobizz.service.DeliveryChallanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dc")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DeliveryChallanController {

    private final DeliveryChallanService service;

    // ✅ FARMER-WISE DC (MAIN FEATURE)
    @GetMapping
    public ResponseEntity<List<DeliveryChallanDto>> getAll(
            @RequestHeader("X-USER-ID") Long userId) {

        return ResponseEntity.ok(service.getByFarmerId(userId));
    }


    // 🔥 ADMIN - GET ALL DC
    @GetMapping("/all")
    public ResponseEntity<List<DeliveryChallanDto>> getAllDC() {

        return ResponseEntity.ok(service.getAllDC());
    }


    // ✅ SCAN DC
    @PostMapping("/scan")
    public ResponseEntity<?> scan(@RequestParam String code) {

        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("❌ Barcode is required");
        }

        try {

            DeliveryChallanDto dto = service.scanAndReturnDC(code);

            if (dto.getStockList() == null || dto.getStockList().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("❌ Invalid DC (No stock data)");
            }

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}