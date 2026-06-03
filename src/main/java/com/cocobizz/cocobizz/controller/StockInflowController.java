package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.StockInflowDto;
import com.cocobizz.cocobizz.security.SecurityUtil;
import com.cocobizz.cocobizz.service.StockInflowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stock-inflow")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StockInflowController {

    private final StockInflowService service;
    private final SecurityUtil securityUtil;

    @PostMapping
    public StockInflowDto create(@RequestBody StockInflowDto dto, HttpServletRequest request){
        Long currentUserId = securityUtil.getCurrentUserId(request);
        return service.createRequest(dto, currentUserId);
    }

    @GetMapping
    public List<StockInflowDto> getAll(HttpServletRequest request){
        Long currentUserId = securityUtil.getCurrentUserId(request);
        return service.getAllRequests(currentUserId);
    }

    @GetMapping("/farmer/{id}")
    public List<StockInflowDto> getByFarmer(
            @PathVariable Long id,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return service.getRequestsByFarmer(id,currentUserId);
    }

    @PutMapping("/approve/{id}")
    public StockInflowDto approve(
            @PathVariable Long id,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return service.approveStock(id,currentUserId);
    }

    @PutMapping("/reject/{id}")
    public String reject(
            @PathVariable Long id,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        service.rejectStock(id,currentUserId);

        return "Rejected";
    }
}