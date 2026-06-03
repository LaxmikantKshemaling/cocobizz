package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.DeliveryChallanDto;
import com.cocobizz.cocobizz.entity.DeliveryChallan;

import java.util.List;
import java.util.Optional;

public interface DeliveryChallanService {

    DeliveryChallan createDC(List<Long> stockIds);

    DeliveryChallanDto scanAndReturnDC(String barcode);

    Optional<DeliveryChallan> findByBarcodeValue(String barcode);

    List<DeliveryChallanDto> getByFarmerId(Long farmerId);

    // 🔥 NEW (ADMIN)
    List<DeliveryChallanDto> getAllDC();
}