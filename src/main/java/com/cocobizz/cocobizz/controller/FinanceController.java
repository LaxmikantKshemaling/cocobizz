package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.FinanceDto;
import com.cocobizz.cocobizz.dto.ProfitGraphDto;
import com.cocobizz.cocobizz.service.FinanceService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/finance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FinanceController {

    private final FinanceService financeService;

    // ✅ GET FINANCE SUMMARY
    @GetMapping
    public FinanceDto getFinance() {
        return financeService.getFinanceData();
    }

    // ✅ GET GRAPH DATA (WEEK / MONTH / YEAR)
    @GetMapping("/graph")
    public List<ProfitGraphDto> getGraph(
            @RequestParam(defaultValue = "WEEK") String type) {

        return financeService.getProfitGraph(type.toUpperCase());
    }
}