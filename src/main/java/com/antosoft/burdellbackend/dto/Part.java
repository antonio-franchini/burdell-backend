package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Part {

    private String partOrderNumber;
    private String partNumber;
    private long cost;
    private String username;
    private String status;
    private String vendorName;
    private String description;

}
