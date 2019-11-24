package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleDetail {
    private String saleMonth;
    private int vehiclesSold;
    private float totalSales;
    private float netIncome;
}