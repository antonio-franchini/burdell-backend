package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Part {

  private long partNumber;
  private long fkOrderId;
  private String status;
  private String description;

}
