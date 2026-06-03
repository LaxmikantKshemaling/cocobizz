package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.VehicleDto;

import java.util.List;

public interface VehicleService {

    VehicleDto createVehicle(VehicleDto dto);

    List<VehicleDto> getAllVehicles();

    VehicleDto getVehicleById(Long id);

    VehicleDto updateVehicle(Long id,VehicleDto dto);

    void deleteVehicle(Long id);

}