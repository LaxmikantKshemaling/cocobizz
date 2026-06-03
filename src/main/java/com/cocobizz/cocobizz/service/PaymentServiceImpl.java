package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.*;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StockInflowRepository stockRepo;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // 🔥 DC SERVICE (IMPORTANT)
    private final DeliveryChallanService dcService;

    // ================= PAY FARMER =================
    @Override
    public String payFarmer(FarmerPaymentDto dto) {

        StockInflow stock = stockRepo.findById(dto.getStockInflowId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (Boolean.TRUE.equals(stock.getPaymentDone())) {
            throw new RuntimeException("Already Paid");
        }

        Users farmer = stock.getFarmer();

        Users admin = userRepository.findById(dto.getPurchaserId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can pay");
        }

        // ✅ PAYMENT
        Payment payment = Payment.builder()
                .paymentId("PAY-" + UUID.randomUUID())
                .stockInflow(stock)
                .farmer(farmer)
                .purchaser(admin)
                .amount(dto.getAmount())
                .currency("INR")
                .status(PaymentStatus.SUCCESS)
                .paymentGateway(PaymentGateway.valueOf(dto.getPaymentMode()))
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // ✅ TRANSACTION
        Transaction txn = Transaction.builder()
                .transactionId("TXN-" + UUID.randomUUID())
                .purchaser(admin)
                .farmer(farmer)
                .amount(dto.getAmount())
                .transactionType(TransactionType.FARMER_PAYMENT)
                .paymentMode(PaymentMode.valueOf(dto.getPaymentMode()))
                .description("Payment to farmer: " + farmer.getUserName())
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(txn);

        // ✅ UPDATE STOCK STATUS
        stock.setPaymentDone(true);
        stock.setStatus(StockStatus.PAID);
        stockRepo.save(stock);

        // 🔥🔥🔥 MOST IMPORTANT LINE
        dcService.createDC(List.of(stock.getId()));

        return "✅ Payment Successful + DC Generated";
    }

    // ================= GET ALL =================
    @Override
    public List<PaymentDto> getAllFarmerPayments() {
        return paymentRepository.findAll().stream().map(this::convert).toList();
    }

    // ================= FARMER VIEW =================
    @Override
    public List<PaymentDto> getPaymentsByFarmer(Long farmerId) {
        return paymentRepository.findByFarmer_UserId(farmerId)
                .stream().map(this::convert).toList();
    }

    // ================= ADMIN VIEW =================
    @Override
    public List<PaymentDto> getPaymentsByAdmin(Long adminId) {
        return paymentRepository.findByPurchaser_UserId(adminId)
                .stream().map(this::convert).toList();
    }

    // ================= UPDATE =================
    @Override
    public PaymentDto updatePayment(Long id, FarmerPaymentDto dto) {

        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        p.setAmount(dto.getAmount());
        p.setPaymentGateway(PaymentGateway.valueOf(dto.getPaymentMode()));

        return convert(paymentRepository.save(p));
    }

    // ================= DELETE =================
    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // ================= TRANSPORT =================
    @Override
    public String payTransport(TransportPaymentDto dto) {

        Users admin = userRepository.findById(dto.getAdminId()).orElseThrow();
        Users transporter = userRepository.findById(dto.getTransporterId()).orElseThrow();

        Transaction txn = Transaction.builder()
                .transactionId("TXN-" + UUID.randomUUID())
                .purchaser(admin)
                .farmer(transporter)
                .amount(dto.getAmount())
                .transactionType(TransactionType.TRANSPORT_PAYMENT)
                .paymentMode(PaymentMode.valueOf(dto.getPaymentMode()))
                .description("Transport Payment")
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(txn);

        return "Transport Payment Successful";
    }

    // ================= DTO =================
    private PaymentDto convert(Payment p) {

        StockInflow s = p.getStockInflow();

        return PaymentDto.builder()
                .id(p.getId())
                .paymentId(p.getPaymentId())
                .stockInflowId(s.getId())
                .farmerName(p.getFarmer().getUserName())
                .purchaserName(p.getPurchaser().getUserName())
                .productName(s.getProductName())
                .categoryName(s.getCategoryName())
                .quantity(s.getQuantity().doubleValue())
                .amount(p.getAmount().doubleValue())
                .status(p.getStatus().name())
                .paymentGateway(p.getPaymentGateway().name())
                .createdAt(p.getCreatedAt())
                .build();
    }
}