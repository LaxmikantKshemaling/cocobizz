package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByStockInflow_Id(Long stockId);

//    List<Shipment> findByOrder_Id(Long orderId);

}