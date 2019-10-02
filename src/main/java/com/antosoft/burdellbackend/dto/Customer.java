package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {

  private long customerId;
  private String role;
  private String type;
  private String address;
  private String email;
  private String phone;
  private String name;
  private String driverLicenceNum;
  private String businessSsn;
  private String businessName;
  private String title;

}
