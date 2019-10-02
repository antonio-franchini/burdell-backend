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
                            "SELECT role FROM profile WHERE username = ? AND password = ?");

            preparedStatement.setString(1, profile.getUsername());
            preparedStatement.setString(2, profile.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                p.setRole(resultSet.getString("role"));
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return p;

    }

}