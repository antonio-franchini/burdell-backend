package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleSearch {

    private Vehicle vehicle;
    private String status; // sold/unsold
    private String permission; // inventory clerk, manager, salesperson, owner

}
