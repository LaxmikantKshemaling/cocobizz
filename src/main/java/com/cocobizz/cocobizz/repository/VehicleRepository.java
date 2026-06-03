package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

}