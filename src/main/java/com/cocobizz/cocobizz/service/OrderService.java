package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.*;
import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getMyOrders(Long userId);

    OrderResponseDto getOrderDetails(Long orderId);

    void placeOrder(Long userId, CreateOrderRequest request);

    List<AdminOrderResponseDto> getAllOrdersAdmin();

    List<AdminPaymentDto> getAllBuyerPayments();

    List<AdminTransactionDto> getAllBuyerTransactions();
}