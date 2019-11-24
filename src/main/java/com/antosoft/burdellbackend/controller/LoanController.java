package com.antosoft.burdellbackend.controller;

import com.antosoft.burdellbackend.dto.Loan;
import com.antosoft.burdellbackend.dto.LoanIncome;
import com.antosoft.burdellbackend.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/getLoan/{vin}")
    public Loan getLoan(@PathVariable String vin) {
        return loanRepository.getLoan(vin);
    }
    @GetMapping("/getLoanIncomeReport")
    public List<LoanIncome> getLoanIncomeReport() {
        return loanRepository.getLoanIncomeReport();
    }

}
