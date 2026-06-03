package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.FinanceDto;
import com.cocobizz.cocobizz.dto.ProfitGraphDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.OrderRepository;
import com.cocobizz.cocobizz.repository.StockInflowRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final OrderRepository orderRepo;
    private final StockInflowRepository stockRepo;

    // ================= FINANCE =================
    @Override
    public FinanceDto getFinanceData() {

        List<Orders> orders = orderRepo.findAll();
        List<StockInflow> inflows = stockRepo.findAll();

        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal costOfGoodsSold = BigDecimal.ZERO;

        Map<String, BigDecimal> costMap = new HashMap<>();

        // ✅ BUILD COST MAP (SAFE)
        for (StockInflow inflow : inflows) {

            if (inflow == null ||
                    inflow.getProductName() == null ||
                    inflow.getQuantity() == null ||
                    inflow.getQuantity().compareTo(BigDecimal.ZERO) <= 0 ||
                    inflow.getTotalAmount() == null ||
                    !Boolean.TRUE.equals(inflow.getPaymentDone())) {
                continue;
            }

            BigDecimal costPerUnit = inflow.getTotalAmount()
                    .divide(inflow.getQuantity(), 2, BigDecimal.ROUND_HALF_UP);

            costMap.put(inflow.getProductName(), costPerUnit);
        }

        // ✅ CALCULATE REVENUE + COST
        for (Orders order : orders) {

            if (order == null ||
                    !"SUCCESS".equalsIgnoreCase(order.getPaymentStatus()) ||
                    order.getItems() == null) continue;

            for (OrderItems item : order.getItems()) {

                if (item == null ||
                        item.getProduct() == null ||
                        item.getProduct().getName() == null ||
                        item.getQuantity() == null ||
                        item.getTotalPrice() == null) continue;

                revenue = revenue.add(item.getTotalPrice());

                BigDecimal costPerUnit = costMap.getOrDefault(
                        item.getProduct().getName(),
                        BigDecimal.ZERO
                );

                BigDecimal cost = costPerUnit.multiply(
                        BigDecimal.valueOf(item.getQuantity())
                );

                costOfGoodsSold = costOfGoodsSold.add(cost);
            }
        }

        BigDecimal profit = revenue.subtract(costOfGoodsSold);

        return FinanceDto.builder()
                .revenue(revenue)
                .purchase(costOfGoodsSold)
                .profit(profit.max(BigDecimal.ZERO))
                .loss(profit.signum() < 0 ? profit.abs() : BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<ProfitGraphDto> getProfitGraph(String type) {

        List<Orders> orders = orderRepo.findAll();
        List<StockInflow> inflows = stockRepo.findAll();

        Map<String, BigDecimal> profitMap = new LinkedHashMap<>();
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();

        Map<Long, BigDecimal> costMap = new HashMap<>();

        // COST MAP
        for (StockInflow inflow : inflows) {

            if (inflow == null ||
                    inflow.getProduct() == null ||
                    inflow.getProduct().getId() == null ||
                    inflow.getQuantity() == null ||
                    inflow.getQuantity().compareTo(BigDecimal.ZERO) <= 0 ||
                    inflow.getTotalAmount() == null ||
                    !Boolean.TRUE.equals(inflow.getPaymentDone())) {
                continue;
            }

            BigDecimal costPerUnit = inflow.getTotalAmount()
                    .divide(inflow.getQuantity(), 2, BigDecimal.ROUND_HALF_UP);

            costMap.put(inflow.getProduct().getId(), costPerUnit);
        }

        // PROCESS ORDERS
        for (Orders order : orders) {

            if (order == null ||
                    !"SUCCESS".equalsIgnoreCase(order.getPaymentStatus()) ||
                    order.getItems() == null ||
                    order.getCreatedAt() == null) continue;

            String key;

            switch (type) {
                case "WEEK":
                    key = order.getCreatedAt().getDayOfWeek().toString();
                    break;
                case "MONTH":
                    key = String.valueOf(order.getCreatedAt().getDayOfMonth());
                    break;
                case "YEAR":
                    key = order.getCreatedAt().getMonth().toString();
                    break;
                default:
                    key = "UNKNOWN";
            }

            BigDecimal orderProfit = BigDecimal.ZERO;
            BigDecimal orderRevenue = BigDecimal.ZERO;

            for (OrderItems item : order.getItems()) {

                if (item == null ||
                        item.getProduct() == null ||
                        item.getProduct().getId() == null ||
                        item.getQuantity() == null ||
                        item.getTotalPrice() == null) continue;

                // 🔥 REVENUE
                orderRevenue = orderRevenue.add(item.getTotalPrice());

                // 🔥 PROFIT
                BigDecimal costPerUnit = costMap.getOrDefault(
                        item.getProduct().getId(),
                        BigDecimal.ZERO
                );

                BigDecimal cost = costPerUnit.multiply(
                        BigDecimal.valueOf(item.getQuantity())
                );

                orderProfit = orderProfit.add(
                        item.getTotalPrice().subtract(cost)
                );
            }

            profitMap.put(
                    key,
                    profitMap.getOrDefault(key, BigDecimal.ZERO).add(orderProfit)
            );

            revenueMap.put(
                    key,
                    revenueMap.getOrDefault(key, BigDecimal.ZERO).add(orderRevenue)
            );
        }

        // RESULT
        List<ProfitGraphDto> result = new ArrayList<>();

        for (String key : profitMap.keySet()) {
            result.add(new ProfitGraphDto(
                    key,
                    profitMap.getOrDefault(key, BigDecimal.ZERO),
                    revenueMap.getOrDefault(key, BigDecimal.ZERO)
            ));
        }

        return result;
    }
}