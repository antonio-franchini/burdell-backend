package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Part;
import com.antosoft.burdellbackend.dto.PartsOrder;
import com.antosoft.burdellbackend.dto.Vehicle;
import com.antosoft.burdellbackend.dto.PartsReport;
import com.antosoft.burdellbackend.repository.PartRepository;
import com.antosoft.burdellbackend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PartController {

    @Autowired
    private PartRepository partRepository;

    @GetMapping("/getParts/{vin}")
    public List<Part> getParts(@PathVariable String vin) {
        return partRepository.getParts(vin);
    }

    @GetMapping("/getParts")
    public List<Part> getParts() {
        return partRepository.getParts();
    }

    @PostMapping("/submitPartStatusChanges")
    public boolean submitPartStatusChanges(@RequestBody List<Part> parts) {
        return partRepository.submitPartStatusChanges(parts);
    }

    @PostMapping("/addPartsOrder")
    public boolean addPartsOrder(@RequestBody PartsOrder partsOrder) {
        return partRepository.addPartsOrder(partsOrder);
    }

    @GetMapping("/getPartsReport")
    public List<PartsReport> getPartsReport() {
        return partRepository.getPartsReport();
    }

}
