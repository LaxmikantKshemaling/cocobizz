package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.VehicleDto;
import com.cocobizz.cocobizz.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class VehicleController {

    private final VehicleService vehicleService;

    // CREATE VEHICLE
    @PostMapping
    public VehicleDto createVehicle(@RequestBody VehicleDto dto){
        return vehicleService.createVehicle(dto);
    }

    // GET ALL VEHICLES
    @GetMapping
    public List<VehicleDto> getAllVehicles(){
        return vehicleService.getAllVehicles();
    }

    // GET VEHICLE BY ID
    @GetMapping("/{id}")
    public VehicleDto getVehicle(@PathVariable Long id){
        return vehicleService.getVehicleById(id);
    }

    // UPDATE VEHICLE
    @PutMapping("/{id}")
    public VehicleDto updateVehicle(@PathVariable Long id,
                                    @RequestBody VehicleDto dto){
        return vehicleService.updateVehicle(id,dto);
    }

    // DELETE VEHICLE
    @DeleteMapping("/{id}")
    public String deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicle(id);
        return "Vehicle deleted successfully";
    }
}