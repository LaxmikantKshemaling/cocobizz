package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.*;
import com.cocobizz.cocobizz.entity.Orders;
import com.cocobizz.cocobizz.repository.OrderRepository;
import com.cocobizz.cocobizz.security.SecurityUtil;
import com.cocobizz.cocobizz.service.OrderService;
import com.cocobizz.cocobizz.service.RazorpayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService service;
    private final SecurityUtil securityUtil;
    private   final OrderRepository orderRepository;
    private  final RazorpayService  razorpayService;

    // ✅ GET ALL ORDERS
    @GetMapping
    public List<OrderResponseDto> getMyOrders(HttpServletRequest request){

        Long userId = securityUtil.getCurrentUserId(request);

        return service.getMyOrders(userId);
    }

    // ✅ GET ORDER DETAILS
    @GetMapping("/{id}")
    public OrderResponseDto getOrderDetails(@PathVariable Long id){

        return service.getOrderDetails(id);
    }


    @GetMapping("/admin/all")
    public List<AdminOrderResponseDto> getAllOrdersAdmin(){
        return service.getAllOrdersAdmin();
    }


    // ✅ ALL BUYER PAYMENTS
    @GetMapping("/payments")
    public List<AdminPaymentDto> getAllBuyerPayments(){
        return service.getAllBuyerPayments();
    }

    // ✅ ALL BUYER TRANSACTIONS
    @GetMapping("/transactions")
    public List<AdminTransactionDto> getAllBuyerTransactions(){
        return service.getAllBuyerTransactions();
    }

    @PostMapping("/create-payment/{orderId}")
    public Map<String, Object> createPayment(@PathVariable Long orderId) {

        Orders order = orderRepository.findById(orderId).orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getId());
        response.put("amount", order.getTotalAmount());

        return response;
    }

    @PostMapping("/create-razorpay-order/{orderId}")
    public Map<String, Object> createRazorpayOrder(@PathVariable Long orderId) throws Exception {

        Orders order = orderRepository.findById(orderId).orElseThrow();

        JSONObject razorOrder = razorpayService.createOrder(
                order.getTotalAmount().doubleValue()
        );

        Map<String, Object> res = new HashMap<>();
        res.put("id", razorOrder.get("id"));
        res.put("amount", order.getTotalAmount());
        res.put("currency", "INR");

        return res;
    }

    @PostMapping("/place")
    public Map<String, Object> placeOrder(
            HttpServletRequest request,
            @RequestBody CreateOrderRequest dto) {

        Long userId = securityUtil.getCurrentUserId(request);

        service.placeOrder(userId, dto);

        // ✅ Get latest order safely
        List<OrderResponseDto> orders = service.getMyOrders(userId);

        if (orders.isEmpty()) {
            throw new RuntimeException("Order not created ❌");
        }

        OrderResponseDto latest = orders.get(0);

        Map<String, Object> res = new HashMap<>();
        res.put("orderId", latest.getOrderId());
        res.put("amount", latest.getTotalAmount());

        return res;
    }


}