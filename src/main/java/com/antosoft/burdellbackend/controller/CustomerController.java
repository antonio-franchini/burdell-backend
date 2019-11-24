package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Customer;
import com.antosoft.burdellbackend.dto.SellerHistory;
import com.antosoft.burdellbackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/getCustomerBuyer/{vin}")
    public Customer getCustomerBuyer(@PathVariable String vin) {
        return customerRepository.getCustomerBuyer(vin);
    }

    @GetMapping("/getCustomerSeller/{vin}")
    public Customer getCustomerSeller(@PathVariable String vin) {
        return customerRepository.getCustomerSeller(vin);
    }

    @GetMapping("/getAllCustomers")
    public ArrayList<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @GetMapping("/sellerHistory")
    public List<SellerHistory> getSellerHistory() {
        return customerRepository.getSellerHistory();
    }

    @PostMapping("/saveCustomer")
    public boolean saveVendor(@RequestBody Customer customer) {
        return customerRepository.saveCustomer(customer);
    }

}
