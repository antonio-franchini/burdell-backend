package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalepersonPerformanceReport {
    private String salepersonFirstName;
    private String salepersonLastName;
    private int vehiclesSold;
    private float totalSales;
}