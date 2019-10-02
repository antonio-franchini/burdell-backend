package com.antosoft.burdellbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Profile {

  private String username;
  private String password;
  private String role;

}
