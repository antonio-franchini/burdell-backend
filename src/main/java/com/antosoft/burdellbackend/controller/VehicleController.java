package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Vehicle;
import com.antosoft.burdellbackend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @PostMapping("/searchVehicles")
    public List<Vehicle> searchVehicles(@RequestBody Vehicle vehicle) {
        return vehicleRepository.searchVehicles();
    }

    @PostMapping("/addVehicle")
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.addVehicle(vehicle);
    }

}
