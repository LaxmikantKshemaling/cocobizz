package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.*;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import com.cocobizz.cocobizz.service.PaymentService;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepo;
    private final TransactionRepository transactionRepo;
    private final OrderItemRepository orderItemRepo;
    private final WarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    // ✅ ONLY ONE FIELD (IMPORTANT)
    @Value("${razorpay.secret}")
    private String razorpaySecret;

    // ================= PAY FARMER =================
    @PostMapping("/farmer")
    public ResponseEntity<String> payFarmer(@RequestBody FarmerPaymentDto dto){
        return ResponseEntity.ok(paymentService.payFarmer(dto));
    }

    // ================= VIEW =================
    @GetMapping("/farmer")
    public List<PaymentDto> getAll(){
        return paymentService.getAllFarmerPayments();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<PaymentDto> getPaymentsByFarmer(@PathVariable Long farmerId){
        return paymentService.getPaymentsByFarmer(farmerId);
    }

    @GetMapping("/admin/{adminId}")
    public List<PaymentDto> getPaymentsByAdmin(@PathVariable Long adminId){
        return paymentService.getPaymentsByAdmin(adminId);
    }

    // ================= TRANSPORT =================
    @PostMapping("/transport")
    public String payTransport(@RequestBody TransportPaymentDto dto){
        return paymentService.payTransport(dto);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public PaymentDto updatePayment(@PathVariable Long id,
                                    @RequestBody FarmerPaymentDto dto){
        return paymentService.updatePayment(id,dto);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable Long id){
        paymentService.deletePayment(id);
        return "Payment deleted successfully";
    }

    // ================= VERIFY NORMAL =================
    @PostMapping("/verify-payment")
    @Transactional
    public String verifyPayment(@RequestBody VerifyPaymentDto dto) {

        Payment payment = paymentRepo.findByPaymentId(dto.getPaymentId());

        if (payment == null) throw new RuntimeException("Payment not found");

        Orders order = payment.getOrder();

        if (dto.isSuccess()) {

            payment.setStatus(PaymentStatus.SUCCESS);

            order.setPaymentStatus("SUCCESS");
            order.setStatus("CONFIRMED");

            reduceStock(order);

            Transaction txn = Transaction.builder()
                    .transactionId("TXN-" + UUID.randomUUID())
                    .purchaser(order.getCustomer())
                    .amount(order.getTotalAmount())
                    .transactionType(TransactionType.CUSTOMER_PAYMENT)
                    .paymentMode(PaymentMode.UPI)
                    .description("Order Payment")
                    .transactionDate(LocalDateTime.now())
                    .build();

            transactionRepo.save(txn);

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setPaymentStatus("FAILED");
        }

        return "Payment Updated";
    }
    @PostMapping("/verify-razorpay")
    @Transactional
    public String verifyRazorpay(@RequestBody RazorpayVerifyDto dto) {

        try {

            // 🔐 STEP 1: SIGNATURE VALIDATION
            String generatedSignature = Utils.getHash(
                    dto.getRazorpayOrderId() + "|" + dto.getPaymentId(),
                    razorpaySecret
            );

            if (!generatedSignature.equals(dto.getSignature())) {
                throw new RuntimeException("Invalid Signature ❌");
            }

            // 🔍 STEP 2: GET ORDER
            Orders order = orderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // 🔍 STEP 3: GET PAYMENT
            Payment payment = paymentRepo.findByOrder(order)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // ✅ STEP 4: UPDATE PAYMENT STATUS
            payment.setStatus(PaymentStatus.SUCCESS);

            // 🔥 IMPORTANT FIX (DO NOT HARD CODE)
            // Keep original payment method selected during checkout
            // OR default to UPI if Razorpay
            if (payment.getPaymentGateway() == null) {
                payment.setPaymentGateway(PaymentGateway.UPI);
            }

            // ✅ STEP 5: UPDATE ORDER
            order.setPaymentStatus("SUCCESS");
            order.setStatus("CONFIRMED");

            // ✅ STEP 6: REDUCE STOCK
            reduceStock(order);

            // ✅ STEP 7: SAVE TRANSACTION
            Transaction txn = Transaction.builder()
                    .transactionId("TXN-" + UUID.randomUUID())
                    .purchaser(order.getCustomer())
                    .amount(order.getTotalAmount())
                    .transactionType(TransactionType.CUSTOMER_PAYMENT)
                    .paymentMode(mapToPaymentMode(payment.getPaymentGateway()))
                    .description("Razorpay Payment")
                    .transactionDate(LocalDateTime.now())
                    .build();

            transactionRepo.save(txn);

            return "Payment Verified ✅";

        } catch (Exception e) {
            throw new RuntimeException("Verification failed: " + e.getMessage(), e);
        }
    }


    @PostMapping("/razorpay-webhook")
    public void webhook(@RequestBody String payload,
                        @RequestHeader("X-Razorpay-Signature") String signature) {

        try {

            boolean valid = Utils.verifySignature(
                    payload,
                    signature,
                    razorpaySecret
            );

            if (!valid) {
                throw new RuntimeException("Invalid webhook ❌");
            }

            System.out.println("Webhook verified ✅");

        } catch (Exception e) {
            throw new RuntimeException("Webhook verification failed: " + e.getMessage(), e);
        }
    }

    // ================= STOCK =================
    private void reduceStock(Orders order) {

        List<OrderItems> items = orderItemRepo.findByOrder_Id(order.getId());

        for (OrderItems item : items) {

            Product product = item.getProduct();
            Warehouse warehouse = product.getWarehouse();

            if (warehouse == null) {
                warehouse = warehouseRepo.findAll().stream()
                        .findFirst()
                        .orElseThrow();
            }

            if (warehouse.getCurrentStock()
                    .compareTo(BigDecimal.valueOf(item.getQuantity())) < 0) {
                throw new RuntimeException("Insufficient stock");
            }

            warehouse.setCurrentStock(
                    warehouse.getCurrentStock()
                            .subtract(BigDecimal.valueOf(item.getQuantity()))
            );

            warehouseRepo.save(warehouse);

            product.setQuantity(
                    product.getQuantity()
                            .subtract(BigDecimal.valueOf(item.getQuantity()))
            );

            productRepo.save(product);
        }
    }


    private PaymentMode mapToPaymentMode(PaymentGateway gateway) {

        return switch (gateway) {
            case CASH -> PaymentMode.CASH;
            case UPI -> PaymentMode.UPI;
            case CARD -> PaymentMode.CARD;
            case BANK_TRANSFER -> PaymentMode.NET_BANKING;
        };
    }
}