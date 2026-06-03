package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.ShipmentDto;
import com.cocobizz.cocobizz.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ShipmentController {

    private final ShipmentService shipmentService;

    // ADMIN ASSIGN VEHICLE
    @PostMapping("/assign")
    public ShipmentDto assignTransport(@RequestBody ShipmentDto dto){
        return shipmentService.assignTransport(dto);
    }


    @PutMapping("/{id}/reject")
    public ShipmentDto reject(@PathVariable Long id){
        return shipmentService.rejectShipment(id);
    }

    // DRIVER ACCEPT SHIPMENT
    @PutMapping("/{id}/accept")
    public ShipmentDto acceptShipment(@PathVariable Long id){
        return shipmentService.acceptShipment(id);
    }

    // DRIVER UPDATE STATUS
    @PutMapping("/{id}/status")
    public ShipmentDto updateStatus(@PathVariable Long id,
                                    @RequestParam String status){
        return shipmentService.updateShipmentStatus(id,status);
    }

    // LIVE LOCATION UPDATE
    @PutMapping("/{id}/location")
    public ShipmentDto updateLocation(@PathVariable Long id,
                                      @RequestParam Double lat,
                                      @RequestParam Double lng){
        return shipmentService.updateLocation(id,lat,lng);
    }

    // GET ALL SHIPMENTS
    @GetMapping
    public List<ShipmentDto> getAllShipments(){
        return shipmentService.getAllShipments();
    }

    // GET SHIPMENT BY ID
    @GetMapping("/{id}")
    public ShipmentDto getShipment(@PathVariable Long id){
        return shipmentService.getShipmentById(id);
    }
}