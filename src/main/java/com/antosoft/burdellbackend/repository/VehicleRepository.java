package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.Date;

@Repository
public class VehicleRepository {

    @Autowired
    DataSource dataSource;

    public List<Vehicle> searchVehicles() {

        List<Vehicle> vehicles = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT * FROM Vehicle");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVin(resultSet.getString("vin"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setMake(resultSet.getString("make"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setYear(resultSet.getString("year"));
                vehicle.setInventoryClerk(resultSet.getString("inventory_clerk"));
                vehicle.setSaleperson(resultSet.getString("saleperson"));
                vehicle.setMileage(resultSet.getInt("mileage"));
                vehicle.setDescription(resultSet.getString("description"));
                vehicle.setFkCustomerBuyerId(resultSet.getInt("fk_customer_buyer_id"));
                vehicle.setFkCustomerSellerId(resultSet.getInt("fk_customer_seller_id"));
                vehicle.setPrice(resultSet.getInt("price"));
                vehicle.setCondition(resultSet.getString("condition"));
                vehicle.setPurchaseDate(resultSet.getDate("purchase_date"));
                vehicle.setColors(resultSet.getString("colors"));
                vehicles.add(vehicle);
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return vehicles;

    }

    public Vehicle addVehicle(Vehicle vehicle) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "INSERT INTO vehicle (vin, type, make, model, year, inventory_clerk, saleperson, mileage, description, fk_customer_buyer_id, fk_customer_seller_id, price, condition, purchase_date, colors) " +
                                    "VALUES ( ?,  ?,  ?,  ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );"
                    );

            preparedStatement.setString(1, vehicle.getVin());
            preparedStatement.setString(2, vehicle.getType());
            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5, vehicle.getYear());
            preparedStatement.setString(6, "inventory clerk");  // TODO: pupulate this based on login info
            preparedStatement.setString(7, null); // saleperson null - not sold yet!
            preparedStatement.setLong(8, vehicle.getMileage());
            preparedStatement.setString(9, vehicle.getDescription());
            preparedStatement.setLong(10, 0); // buyer null - not sold yet!
            preparedStatement.setLong(11, 0); // TODO: pupulate this with seller info?
            preparedStatement.setLong(12, vehicle.getPrice());
            preparedStatement.setString(13, vehicle.getCondition());
            preparedStatement.setDate(14, vehicle.getPurchaseDate());
            preparedStatement.setString(15, vehicle.getColors());

            numInsertions = preparedStatement.executeUpdate();
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return numInsertions == 0 ? null : vehicle;

    }

}