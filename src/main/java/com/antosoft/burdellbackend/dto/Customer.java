package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {

    /* individual or business */
    private boolean business;

    /* general info */
    private long customerId;
    private String email;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    /* individual info */
    private String driverLicense;
    private String firstName;
    private String lastName;

    /* business info */
    private String taxId;
    private String name;
    private String contactName;
    private String contactTitle;

}
