package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.Vendor;
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
public class VendorRepository {

    @Autowired
    DataSource dataSource;


    public List<Vendor> getVendors() {
        List<Vendor> vendors = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT name, phone, street, city, state, zip_code FROM vendor");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vendor vendor = new Vendor();
                vendor.setName(resultSet.getString("name"));
                vendor.setPhone(resultSet.getString("phone"));
                vendor.setStreet(resultSet.getString("street"));
                vendor.setCity(resultSet.getString("city"));
                vendor.setState(resultSet.getString("state"));
                vendor.setZipCode(resultSet.getString("zip_code"));
                vendors.add(vendor);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return vendors;
    }


    public boolean saveVendor(Vendor vendor) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "INSERT INTO vendor (name, phone, street, city, state, zip_code)" +
                                    " VALUES ( ?, ?, ?, ?, ?, ? );"
                    );

            preparedStatement.setString(1, vendor.getName());
            preparedStatement.setString(2, vendor.getPhone());
            preparedStatement.setString(3, vendor.getStreet());
            preparedStatement.setString(4, vendor.getCity());
            preparedStatement.setString(5, vendor.getState());
            preparedStatement.setString(6, vendor.getZipCode());

            numInsertions = preparedStatement.executeUpdate();

            if (numInsertions == 0) {
                return false;
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return numInsertions > 0;

    }


}