package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Loan {

  private long loanId;
  private java.sql.Date startMonth;
  private long term;
  private long payment;
  private long interest;
  private long downPayment;
  private long fkCustomerId;

}
