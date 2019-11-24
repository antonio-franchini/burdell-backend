package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartsReport {
  private String vendorName;
  private int numPartsSold;
  private float totalDollarAmount;
}
