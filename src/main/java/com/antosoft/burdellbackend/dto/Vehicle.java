package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Vehicle {

    private String vin;
    private String type;
    private String make;
    private String model;
    private String year;
    private String inventoryClerk;
    private String saleperson;
    private long mileage;
    private String description;
    private long fkCustomerBuyerId;
    private long fkCustomerSellerId;
    private long price;
    private String condition;
    private java.sql.Date purchaseDate;
    private String colors;

}
