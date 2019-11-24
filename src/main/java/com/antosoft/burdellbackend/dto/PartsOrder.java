package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class PartsOrder {

  private String username;
  private String vin;
  private String vendorName;

  private ArrayList<String> parts;

}
