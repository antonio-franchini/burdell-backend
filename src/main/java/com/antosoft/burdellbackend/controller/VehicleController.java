package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Color;
import com.antosoft.burdellbackend.dto.SaleOrder;
import com.antosoft.burdellbackend.dto.SaleDetail;
import com.antosoft.burdellbackend.dto.Vehicle;
import com.antosoft.burdellbackend.dto.VehicleAvgDays;
import com.antosoft.burdellbackend.dto.VehicleMake;
import com.antosoft.burdellbackend.dto.VehicleSearch;
import com.antosoft.burdellbackend.dto.VehicleType;
import com.antosoft.burdellbackend.dto.PricePerCondition;
import com.antosoft.burdellbackend.dto.SalepersonPerformanceReport;
import com.antosoft.burdellbackend.repository.LoanRepository;
import com.antosoft.burdellbackend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LoanRepository loanRepository;

    @PostMapping("/searchVehicles")
    public List<Vehicle> searchVehicles(@RequestBody VehicleSearch vehicleSearch) {
        return vehicleRepository.searchVehicles(vehicleSearch);
    }

    @PostMapping("/addVehicle")
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.addVehicle(vehicle);
    }

    @GetMapping("/getVehicle/{vin}")
    public Vehicle getVehicle(@PathVariable String vin) {
        return vehicleRepository.getVehicle(vin);
    }

    @PostMapping("/addSaleOrder")
    public boolean addSaleOrder(@RequestBody SaleOrder saleOrder) {
        boolean saleInfoSuccess = vehicleRepository.addSaleInfo(saleOrder);
        boolean loanSuccess = true;

        if(saleOrder.getLoan() != null){
            loanSuccess = loanRepository.addLoan(saleOrder.getLoan());
        }
        return saleInfoSuccess && loanSuccess;
    }
    @GetMapping("/getAvgDaysReport")
    public List<VehicleAvgDays> getAvgDaysReport() {
        return vehicleRepository.getVehicleAveragesDaysReport();
    }

    @GetMapping("/getPricePerConditionReport")
    public List<PricePerCondition> getPricePerConditionReport() {
        return vehicleRepository.getPricePerConditionReport();
    }
    @GetMapping("/getSaleDetailByDateReport")
    public List<SaleDetail> getSaleDetailByDateReport() {
        return vehicleRepository.getSaleDetailByDateReport();
    }

    @GetMapping("/getSalepersonPerformanceReport/{saleMonth}")
    public List<SalepersonPerformanceReport> getSalepersonPerformanceReport(@PathVariable String saleMonth) {
        return vehicleRepository.getSalepersonPerformanceReport(saleMonth);
    }

    @GetMapping("/getVehicleTypes")
    public List<VehicleType> getVehicleTypes() {
        return vehicleRepository.getVehicleTypes();
    }

    @GetMapping("/getVehicleMakes")
    public List<VehicleMake> getVehicleMakes() {
        return vehicleRepository.getVehicleMakes();
    }

    @GetMapping("/getColors")
    public List<Color> getColors() {
        return vehicleRepository.getColors();
    }
}
