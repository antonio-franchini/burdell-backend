package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartOrder {

  private long orderId;
  private String vendor;
  private String address;
  private String phone;

}
