package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.StockInflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInflowRepository extends JpaRepository<StockInflow, Long> {

    List<StockInflow> findByFarmer_UserId(Long farmerId);
}