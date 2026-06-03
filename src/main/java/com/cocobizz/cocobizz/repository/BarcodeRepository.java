package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarcodeRepository extends JpaRepository<Barcode, Long> {

    Optional<Barcode> findByBarcodeValue(String value);
}