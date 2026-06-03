package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.*;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final OrderItemRepository orderItemRepo;
    private final PaymentRepository paymentRepo;
    private final TransactionRepository transactionRepo;
    private  final  ProductRepository  productRepo;

    private final WarehouseRepository warehouseRepo;

    // ================= GET MY ORDERS =================
    @Override
    public List<OrderResponseDto> getMyOrders(Long userId) {

        List<Orders> orders = orderRepo.findByCustomer_UserId(userId);

        return orders.stream()
                .map(this::mapOrder)
                .toList();
    }


    @Override
    public List<AdminOrderResponseDto> getAllOrdersAdmin() {

        return orderRepo.findAll()
                .stream()
                .map(this::mapAdminOrder)
                .toList();
    }

    // ================= ORDER DETAILS =================
    @Override
    public OrderResponseDto getOrderDetails(Long orderId) {

        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapOrder(order);
    }


    @Override
    public List<AdminPaymentDto> getAllBuyerPayments() {

        return paymentRepo.findAll()
                .stream()

                // ✅ ONLY CUSTOMER PAYMENTS
                .filter(p -> p.getOrder() != null)

                .map(p -> AdminPaymentDto.builder()
                        .paymentId(p.getPaymentId())
                        .buyerName(p.getPurchaser().getUserName())
                        .buyerEmail(p.getPurchaser().getEmail())
                        .amount(p.getAmount())
                        .status(p.getStatus().name())
                        .paymentMode(p.getPaymentGateway().name())
                        .orderNumber(p.getOrder().getOrderNumber())
                        .createdAt(p.getCreatedAt())
                        .build()
                ).toList();
    }



    @Override
    public List<AdminTransactionDto> getAllBuyerTransactions() {

        return transactionRepo.findAll()
                .stream()

                // ✅ ONLY CUSTOMER PAYMENT
                .filter(t -> t.getTransactionType() == TransactionType.CUSTOMER_PAYMENT)

                .map(t -> AdminTransactionDto.builder()
                        .transactionId(t.getTransactionId())
                        .buyerName(t.getPurchaser().getUserName())
                        .buyerEmail(t.getPurchaser().getEmail())
                        .amount(t.getAmount())
                        .type(t.getTransactionType().name())
                        .paymentMode(t.getPaymentMode().name())
                        .description(t.getDescription())
                        .date(t.getTransactionDate())
                        .build()
                ).toList();
    }


    // ================= PLACE ORDER =================
    @Override
    @Transactional
    public void placeOrder(Long userId, CreateOrderRequest request) {

        Cart cart = cartRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Users buyer = cart.getUser();

        // ✅ STEP 1: CREATE ORDER (PENDING)
        Orders order = Orders.builder()
                .orderNumber("ORD-" + System.currentTimeMillis())
                .customer(buyer)
                .totalAmount(cart.getTotalAmount())
                .status("PENDING") // ✅ CORRECT
                .paymentStatus("PENDING") // ✅ CORRECT
                .orderDate(LocalDateTime.now())
                .deliveryAddress(request.getAddress())
                .build();

        order = orderRepo.save(order);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }

        // ================= ITEMS =================
        for (CartItem c : cart.getItems()) {

            Product product = productRepo.findById(c.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItems oi = new OrderItems();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(c.getQuantity());
            oi.setUnitPrice(c.getUnitPrice());
            oi.setTotalPrice(c.getTotalPrice());

            order.getItems().add(oi);
            orderItemRepo.save(oi);
        }

        // ================= PAYMENT (PENDING) =================
        PaymentGateway gateway = mapToPaymentGateway(request.getPaymentMethod());

        Payment payment = Payment.builder()
                .paymentId("PAY-" + UUID.randomUUID())
                .order(order)
                .purchaser(buyer)
                .amount(order.getTotalAmount())
                .currency("INR")
                .status(PaymentStatus.PENDING) // ✅ FIXED
                .paymentGateway(gateway)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepo.save(payment);

        // ❌ REMOVE TRANSACTION HERE (ONLY AFTER SUCCESS)

        // ================= CLEAR CART =================
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        cartRepo.save(cart);
    }
    private PaymentGateway mapToPaymentGateway(String method) {

        if (method == null) return PaymentGateway.CASH;

        return switch (method.toUpperCase()) {
            case "COD" -> PaymentGateway.CASH;
            case "UPI" -> PaymentGateway.UPI;
            case "CARD" -> PaymentGateway.CARD;
            case "BANK", "NET_BANKING" -> PaymentGateway.BANK_TRANSFER;
            default -> throw new RuntimeException("Invalid payment method: " + method);
        };
    }
    private PaymentMode mapToPaymentMode(String method) {

        if (method == null) return PaymentMode.CASH;

        return switch (method.toUpperCase()) {
            case "COD" -> PaymentMode.CASH;
            case "UPI" -> PaymentMode.UPI;
            case "CARD" -> PaymentMode.CARD;
            case "BANK", "NET_BANKING" -> PaymentMode.NET_BANKING;
            default -> throw new RuntimeException("Invalid payment method: " + method);
        };
    }


    private AdminOrderResponseDto mapAdminOrder(Orders order){

        List<OrderItems> items = orderItemRepo.findByOrder_Id(order.getId());

        Payment payment = paymentRepo.findByOrder(order)
                .stream()
                .findFirst()
                .orElse(null);

        return AdminOrderResponseDto.builder()

                // ORDER
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())

                // BUYER
                .buyerName(order.getCustomer().getUserName())
                .buyerEmail(order.getCustomer().getEmail())
                .address(order.getDeliveryAddress())

                // PAYMENT
                .paymentStatus(order.getPaymentStatus())
                .paymentMode(payment != null ? payment.getPaymentGateway().name() : "COD")

                // TOTAL
                .totalAmount(order.getTotalAmount())

                // ITEMS
                .items(items.stream().map(i ->
                        OrderItemDto.builder()
                                .productName(i.getProduct().getName())
                                .imageUrl(i.getProduct().getImageUrl())
                                .quantity(i.getQuantity())
                                .price(i.getUnitPrice())
                                .total(i.getTotalPrice())
                                .build()
                ).toList())

                .build();
    }

    // ================= MAPPER =================
    private OrderResponseDto mapOrder(Orders order){

        List<OrderItems> items = orderItemRepo.findByOrder_Id(order.getId());

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .orderDate(order.getOrderDate())
                .address(order.getDeliveryAddress())
                .items(items.stream().map(i ->
                        OrderItemDto.builder()
                                .productName(i.getProduct().getName())
                                .imageUrl(i.getProduct().getImageUrl())
                                .quantity(i.getQuantity())
                                .price(i.getUnitPrice())
                                .total(i.getTotalPrice())
                                .build()
                ).toList())
                .build();
    }
}