package com.antosoft.burdellbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class DatabaseConnection {

    @Autowired
    DatabaseConfiguration databaseConfiguration;

    public DatabaseConnection(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    @Bean(name = "dataSource")
    public DataSource db2DataSource() {
        DriverManagerDataSource dmds = new DriverManagerDataSource();
        dmds.setUrl(databaseConfiguration.getUrl());
        dmds.setUsername(databaseConfiguration.getUsername());
        dmds.setPassword(databaseConfiguration.getPassword());
        dmds.setDriverClassName(databaseConfiguration.getDriverClassName());
        return dmds;
    }

}

