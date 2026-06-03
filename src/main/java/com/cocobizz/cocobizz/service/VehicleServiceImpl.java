package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.VehicleDto;
import com.cocobizz.cocobizz.entity.Vehicle;
import com.cocobizz.cocobizz.entity.VehicleStatus;
import com.cocobizz.cocobizz.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepo;

    @Override
    public VehicleDto createVehicle(VehicleDto dto){

        // ✅ ROLE CHECK
        if(!"DISTRIBUTOR".equalsIgnoreCase(dto.getStatus()) && dto.getVehicleNumber()==null){
            throw new RuntimeException("Only Distributor can add vehicle");
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleNumber(dto.getVehicleNumber())
                .driverName(dto.getDriverName())
                .driverPhone(dto.getDriverPhone())
                .capacity(dto.getCapacity())
                .currentLocation(dto.getCurrentLocation())
                .status(VehicleStatus.valueOf(dto.getStatus()))
                .build();

        return convertToDto(vehicleRepo.save(vehicle));
    }

    @Override
    public List<VehicleDto> getAllVehicles(){

        return vehicleRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public VehicleDto getVehicleById(Long id){

        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        return convertToDto(vehicle);
    }

    @Override
    public VehicleDto updateVehicle(Long id,VehicleDto dto){

        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setDriverName(dto.getDriverName());
        vehicle.setDriverPhone(dto.getDriverPhone());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setCurrentLocation(dto.getCurrentLocation());
        vehicle.setStatus(VehicleStatus.valueOf(dto.getStatus()));

        vehicleRepo.save(vehicle);

        return convertToDto(vehicle);
    }

    @Override
    public void deleteVehicle(Long id){

        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicleRepo.delete(vehicle);
    }

    private VehicleDto convertToDto(Vehicle vehicle){

        return VehicleDto.builder()
                .id(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .driverName(vehicle.getDriverName())
                .driverPhone(vehicle.getDriverPhone())
                .capacity(vehicle.getCapacity())
                .currentLocation(vehicle.getCurrentLocation())
                .status(vehicle.getStatus().name())
                .build();
    }
}