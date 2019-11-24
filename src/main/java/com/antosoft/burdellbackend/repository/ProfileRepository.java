package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.Profile;
import com.antosoft.burdellbackend.dto.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ProfileRepository {

    @Autowired
    DataSource dataSource;


    public Profile login(Profile profile) {
        Profile p = new Profile();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT username, password, permission, first_name, last_name FROM profile WHERE username = ? AND password = ?");

            preparedStatement.setString(1, profile.getUsername());
            preparedStatement.setString(2, profile.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                p.setUsername(resultSet.getString("username"));
                p.setPassword(resultSet.getString("password"));
                p.setPermission(resultSet.getString("permission"));
                p.setFirstName(resultSet.getString("first_name"));
                p.setLastName(resultSet.getString("last_name"));
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return p;
    }


    public Profile getSaleperson(String vin) {
        Profile p = null;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT username, password, permission, first_name, last_name FROM profile " +
                                    "WHERE username in (SELECT saleperson_name FROM vehicle where vin = ?)");

            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                p = new Profile();
                p.setUsername(resultSet.getString("username"));
                p.setPassword(resultSet.getString("password"));
                p.setPermission(resultSet.getString("permission"));
                p.setFirstName(resultSet.getString("first_name"));
                p.setLastName(resultSet.getString("last_name"));
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return p;
    }


    public Profile getInventoryClerk(String vin) {
        Profile p = null;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT username, password, permission, first_name, last_name FROM profile " +
                                    "WHERE username in (SELECT inventory_clerk_name FROM vehicle where vin = ?)");

            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                p = new Profile();
                p.setUsername(resultSet.getString("username"));
                p.setPassword(resultSet.getString("password"));
                p.setPermission(resultSet.getString("permission"));
                p.setFirstName(resultSet.getString("first_name"));
                p.setLastName(resultSet.getString("last_name"));
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return p;
    }

}