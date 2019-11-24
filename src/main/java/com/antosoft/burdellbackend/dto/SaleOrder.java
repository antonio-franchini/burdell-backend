package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleOrder {

    private String vin;
    private String salepersonName;
    private java.sql.Date saleDate;
    private Long buyerCustomerId;
    private Loan loan;

}
