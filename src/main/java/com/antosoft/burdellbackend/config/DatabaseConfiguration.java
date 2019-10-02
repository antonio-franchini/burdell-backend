package com.antosoft.burdellbackend.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="spring.datasource")
@Component
@Data
@NoArgsConstructor
public class DatabaseConfiguration {
    String url;
    String username;
    String password;
    String driverClassName;

}
