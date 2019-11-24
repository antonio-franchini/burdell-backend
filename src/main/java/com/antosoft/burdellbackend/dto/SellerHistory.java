package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SellerHistory {
    private String name;
    private int sellerCustomerId;
    private int totalVehiclesSold;
    private float avgVehiclePrice;
    private float avgParts;
    private float avgPartsCost;
}