package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vehicle {

    private String vin;
    private String type;
    private String make;
    private String model;
    private long year;
    private String inventoryClerkName;
    private String salepersonName;
    private long mileage;
    private String description;
    private long purchasePrice;
    private String condition;
    private java.sql.Date purchaseDate;
    private java.sql.Date saleDate;
    private long buyerCustomerId;
    private long sellerCustomerId;
    private String colors;

}
