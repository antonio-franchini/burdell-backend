package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.Part;
import com.antosoft.burdellbackend.dto.PartsOrder;
import com.antosoft.burdellbackend.dto.Vehicle;
import com.antosoft.burdellbackend.dto.PartsReport;
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
public class PartRepository {

    @Autowired
    DataSource dataSource;

    public List<Part> getParts(String vin) {
        List<Part> parts = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT po.part_order_number, po.part_number, po.username, po.cost, po.status, po.vendor_name, p.description " +
                                    "FROM part_order as po JOIN part as p ON p.part_number = po.part_number AND po.vin = ?");
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Part part = new Part();
                part.setPartOrderNumber(resultSet.getString("part_order_number"));
                part.setPartNumber(resultSet.getString("part_number"));
                part.setUsername(resultSet.getString("username"));
                part.setCost(resultSet.getLong("cost"));
                part.setStatus(resultSet.getString("status"));
                part.setVendorName(resultSet.getString("vendor_name"));
                part.setDescription(resultSet.getString("description"));
                parts.add(part);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return parts;
    }


    public List<Part> getParts() {
        List<Part> parts = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT part_number, description FROM part");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Part part = new Part();
                part.setPartOrderNumber("");
                part.setPartNumber(resultSet.getString("part_number"));
                part.setUsername(null);
                part.setCost(0L);
                part.setStatus(null);
                part.setVendorName(null);
                part.setDescription(resultSet.getString("description"));
                parts.add(part);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return parts;
    }


    public boolean addPartsOrder(PartsOrder partsOrder) {
        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "INSERT INTO part_order (part_order_number, part_number, username, vin, cost, status, vendor_name, batch_number) " +
                                    " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);"
                    );

            Long batchNumber = getMaxBatchNumber(partsOrder.getVin());

            for (int i = 0; i < partsOrder.getParts().size(); i++) {

                preparedStatement.setString(1, partsOrder.getVin() + "-" + batchNumber);
                preparedStatement.setString(2, partsOrder.getParts().get(i));
                preparedStatement.setString(3, partsOrder.getUsername());
                preparedStatement.setString(4, partsOrder.getVin());
                preparedStatement.setLong(5, 0L);
                preparedStatement.setString(6, "ordered");
                preparedStatement.setString(7, partsOrder.getVendorName());
                preparedStatement.setLong(8, batchNumber);

                numInsertions = preparedStatement.executeUpdate();

                if (numInsertions == 0) {
                    return false;
                }
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return numInsertions > 0;
    }


    public Long getMaxBatchNumber(String vin) {
        Long maxBatchNumber = 0L;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT MAX(batch_number) FROM part_order where vin = ?"
                    );

            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                maxBatchNumber = resultSet.getLong("max") + 1;
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return 0L;
        }
        return maxBatchNumber;
    }

    public List<PartsReport> getPartsReport() {
        List<PartsReport> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT vendor_name, COUNT(*) as number_of_parts_sold, SUM(cost) as total_dollar_amount FROM part_order GROUP BY vendor_name");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PartsReport partsReport = new PartsReport();
                partsReport.setVendorName(resultSet.getString("vendor_name"));
                partsReport.setNumPartsSold(resultSet.getInt("number_of_parts_sold"));
                partsReport.setTotalDollarAmount(resultSet.getFloat("total_dollar_amount"));
                report.add(partsReport);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }


    public boolean submitPartStatusChanges(List<Part> parts) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "UPDATE  part_order SET" +
                                    " status = ? " +
                                    " WHERE part_order_number = ? AND part_number = ?"
                    );

            for (int i = 0; i < parts.size(); i++) {
                preparedStatement.setString(1, parts.get(i).getStatus());
                preparedStatement.setString(2, parts.get(i).getPartOrderNumber());
                preparedStatement.setString(3, parts.get(i).getPartNumber());

                numInsertions = preparedStatement.executeUpdate();

                if (numInsertions == 0) {
                    return false;
                }


            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return numInsertions > 0;

    }

}