package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.StockOutflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockOutflowRepository extends JpaRepository<StockOutflow, Long> {

}