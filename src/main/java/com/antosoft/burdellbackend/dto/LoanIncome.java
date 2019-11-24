package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanIncome {
    private int year;
    private int month;
    private float monthlyPaymentTotal;
    private float monthlyShare;
}
