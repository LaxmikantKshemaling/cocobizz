package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.StockInflowDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockInflowServiceImpl implements StockInflowService {

    private final StockInflowRepository repo;
    private final WarehouseRepository warehouseRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    // ================= CREATE =================
    @Override
    public StockInflowDto createRequest(StockInflowDto dto, Long currentUserId) {

        Users farmer = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (farmer.getRole() != Role.FARMER) {
            throw new RuntimeException("Only FARMER can send request");
        }

        Product product = null;

        // ✅ EXISTING PRODUCT
        if (dto.getProductId() != null) {
            product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
        }

        Warehouse warehouse = warehouseRepo.findById(dto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        // ✅ SAVE PRODUCT NAME ALWAYS
        String finalProductName =
                product != null ? product.getName() : dto.getProductName();

        StockInflow stock = StockInflow.builder()
                .product(product)
                .productName(finalProductName) // ✅ FIXED
                .categoryName(dto.getCategoryName())
                .unitType(dto.getUnitType())
                .farmer(farmer)
                .warehouse(warehouse)
                .quantity(dto.getQuantity())
                .totalAmount(dto.getTotalAmount())
                .inflowDate(LocalDate.now())
                .status(StockStatus.PENDING)
                .paymentDone(false)
                .build();

        return convert(repo.save(stock));
    }
    // ================= ADMIN =================
    @Override
    public List<StockInflowDto> getAllRequests(Long currentUserId) {

        Users user = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN allowed");
        }

        return repo.findAll().stream().map(this::convert).toList();
    }

    // ================= FARMER =================
    @Override
    public List<StockInflowDto> getRequestsByFarmer(Long farmerId, Long currentUserId) {

        if (!farmerId.equals(currentUserId)) {
            throw new RuntimeException("Unauthorized");
        }

        return repo.findByFarmer_UserId(farmerId)
                .stream()
                .map(this::convert)
                .toList();
    }

    // ================= APPROVE =================
    @Override
    public StockInflowDto approveStock(Long id, Long currentUserId) {

        Users admin = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN");
        }

        StockInflow stock = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        stock.setStatus(StockStatus.APPROVED);

        return convert(repo.save(stock));
    }
    // ================= REJECT =================
    @Override
    public void rejectStock(Long id, Long currentUserId) {

        Users admin = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN");
        }

        StockInflow stock = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        stock.setStatus(StockStatus.REJECTED);

        repo.save(stock);
    }

    // ================= CONVERT =================
    private StockInflowDto convert(StockInflow s) {

        return StockInflowDto.builder()
                .id(s.getId())

                // ✅ SAFE HANDLING
                .productId(s.getProduct() != null ? s.getProduct().getId() : null)

                .productName(
                        s.getProduct() != null
                                ? s.getProduct().getName()
                                : s.getProductName() // ✅ FIXED
                )

                .categoryName(s.getCategoryName())
                .unitType(s.getUnitType())

                .farmerId(s.getFarmer().getUserId())
                .farmerUserName(s.getFarmer().getUserName())
                .farmerPhone(s.getFarmer().getPhone())
                .farmerAddress(s.getFarmer().getAddress())

                .warehouseId(s.getWarehouse().getId())
                .warehouseName(s.getWarehouse().getName())

                .quantity(s.getQuantity())
                .totalAmount(s.getTotalAmount())

                .status(s.getStatus().name())
                .paymentDone(s.getPaymentDone())

                .build();
    }
}