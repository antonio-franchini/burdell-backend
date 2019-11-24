package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PricePerCondition {
  private String type;
  private float excellent;
  private float veryGood;
  private float good;
  private float fair;
}
