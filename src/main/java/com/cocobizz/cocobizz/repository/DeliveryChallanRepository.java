package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.DeliveryChallan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryChallanRepository
        extends JpaRepository<DeliveryChallan, Long> {

    Optional<DeliveryChallan> findByBarcodeValue(String barcode);

    // ✅ REQUIRED FOR FARMER FILTER
    List<DeliveryChallan> findByFarmerId(Long farmerId);
}