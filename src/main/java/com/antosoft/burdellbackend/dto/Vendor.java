package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vendor {
  private String name;
  private String phone;
  private String street;
  private String city;
  private String state;
  private String zipCode;
}
