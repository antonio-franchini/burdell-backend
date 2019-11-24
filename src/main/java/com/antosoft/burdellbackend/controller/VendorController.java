package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Vendor;
import com.antosoft.burdellbackend.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping("/getVendors")
    public List<Vendor> getVendors() {
        return vendorRepository.getVendors();
    }

    @PostMapping("/saveVendor")
    public boolean saveVendor(@RequestBody Vendor vendor) {
        return vendorRepository.saveVendor(vendor);
    }


}
