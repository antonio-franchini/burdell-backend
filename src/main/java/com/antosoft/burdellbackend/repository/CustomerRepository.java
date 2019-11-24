package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.Customer;
import com.antosoft.burdellbackend.dto.SellerHistory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class CustomerRepository {

    @Autowired
    DataSource dataSource;

    public List<SellerHistory> getSellerHistory() {
        List<SellerHistory> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT top.name, top.seller_customer_id, top.total_vehicles_sold, top.average_vehicle_price, COALESCE(z.av_parts, 0) AS av_parts, COALESCE(z.av_cost_parts, 0) AS av_cost_parts FROM(SELECT a.name, b.seller_customer_id, COUNT(*) AS total_vehicles_sold, AVG(b.purchase_price) AS average_vehicle_price FROM vehicle b JOIN( SELECT name, customer_id FROM business WHERE customer_id IN ( SELECT seller_customer_id FROM vehicle) UNION SELECT CONCAT(first_name, ' ', last_name) AS name, customer_id FROM individual WHERE customer_id IN ( SELECT DISTINCT seller_customer_id FROM vehicle ) ) a ON (a.customer_id = b.seller_customer_id) GROUP BY a.name, b.seller_customer_id ) top LEFT JOIN ( SELECT bot.seller_customer_id, bot.av_parts, bot.av_cost_parts FROM ( SELECT b.seller_customer_id, AVG(b.number_of_parts_sold) AS av_parts, AVG(b.total_dollar_amount) AS av_cost_parts FROM ( SELECT v.seller_customer_id, a.vin, COUNT(*) as number_of_parts_sold, AVG(a.cost) AS total_dollar_amount FROM part_order a JOIN vehicle v ON a.vin = v.vin GROUP BY a.vin, v.seller_customer_id ) b GROUP BY b.seller_customer_id ) bot ) z ON top.seller_customer_id = z.seller_customer_id ORDER BY top.total_vehicles_sold DESC, top.average_vehicle_price ASC");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SellerHistory sellerHistory = new SellerHistory();
                sellerHistory.setName(resultSet.getString("name"));
                sellerHistory.setSellerCustomerId(resultSet.getInt("seller_customer_id"));
                sellerHistory.setTotalVehiclesSold(resultSet.getInt("total_vehicles_sold"));
                sellerHistory.setAvgVehiclePrice(resultSet.getFloat("average_vehicle_price"));
                sellerHistory.setAvgParts(resultSet.getFloat("av_parts"));
                sellerHistory.setAvgPartsCost(resultSet.getFloat("av_cost_parts"));
                report.add(sellerHistory);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }

    public Customer getCustomerSeller(String vin) {
        Customer customer = null;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT customer_id, email, phone, street, city, state, zip_code " +
                                    "FROM customer " +
                                    "WHERE customer_id IN (SELECT seller_customer_id FROM vehicle WHERE vin = ?)");
            preparedStatement.setString(1, vin);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer();
                customer.setCustomerId(resultSet.getLong("customer_id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setStreet(resultSet.getString("street"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
                customer.setZipCode(resultSet.getString("zip_code"));
            }

            /* Check if customer is a business */
            preparedStatement =
                    con.prepareStatement(
                            "SELECT tax_id, name, contact_name, contact_title " +
                                    "FROM business " +
                                    "WHERE customer_id IN (SELECT seller_customer_id FROM vehicle WHERE vin = ?)");
            preparedStatement.setString(1, vin);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next() && customer != null) {
                customer.setBusiness(true);
                customer.setTaxId(rs.getString("tax_id"));
                customer.setName(rs.getString("name"));
                customer.setContactName(rs.getString("contact_name"));
                customer.setContactTitle(rs.getString("contact_title"));
            }

            /* Check if customer is an individual */
            preparedStatement =
                    con.prepareStatement(
                            "SELECT driver_license, first_name, last_name " +
                                    "FROM individual " +
                                    "WHERE customer_id IN (SELECT seller_customer_id FROM vehicle WHERE vin = ?)");
            preparedStatement.setString(1, vin);
            rs = preparedStatement.executeQuery();

            if (rs.next() && customer != null) {
                customer.setBusiness(false);
                customer.setDriverLicense(rs.getString("driver_license"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return customer;
    }

    public Customer getCustomerBuyer(String vin) {
        Customer customer = null;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT customer_id, email, phone, street, city, state, zip_code " +
                                    "FROM customer " +
                                    "WHERE customer_id IN (SELECT buyer_customer_id FROM vehicle WHERE vin = ?)");
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer();
                customer.setCustomerId(resultSet.getLong("customer_id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setStreet(resultSet.getString("street"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
                customer.setZipCode(resultSet.getString("zip_code"));
            }

            /* Check if customer is a business */
            preparedStatement =
                    con.prepareStatement(
                            "SELECT tax_id, name, contact_name, contact_title\n" +
                                    "FROM business\n" +
                                    "WHERE customer_id IN (SELECT buyer_customer_id FROM vehicle WHERE vin = ?)");
            preparedStatement.setString(1, vin);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next() && customer != null) {
                customer.setBusiness(true);
                customer.setTaxId(rs.getString("tax_id"));
                customer.setName(rs.getString("name"));
                customer.setContactName(rs.getString("contact_name"));
                customer.setContactTitle(rs.getString("contact_title"));
            }

            /* Check if customer is an individual */
            preparedStatement =
                    con.prepareStatement(
                            "SELECT driver_license, first_name, last_name " +
                                    "FROM individual " +
                                    "WHERE customer_id IN (SELECT buyer_customer_id FROM vehicle WHERE vin = ?)");
            preparedStatement.setString(1, vin);
            rs = preparedStatement.executeQuery();

            if (rs.next() && customer != null) {
                customer.setBusiness(false);
                customer.setDriverLicense(rs.getString("driver_license"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return customer;
    }


    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT customer_id, email, phone, street, city, state, zip_code " +
                                    "FROM customer");
            ResultSet resultSet = preparedStatement.executeQuery();

            int customerIdx = 0;

            while (resultSet.next()) {
                Customer customer = new Customer();

                customer.setCustomerId(resultSet.getLong("customer_id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setStreet(resultSet.getString("street"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
                customer.setZipCode(resultSet.getString("zip_code"));

                /* Check if customer is a business */
                preparedStatement =
                        con.prepareStatement(
                                "SELECT tax_id, name, contact_name, contact_title\n" +
                                        "FROM business\n" +
                                        "WHERE customer_id = ?");
                preparedStatement.setLong(1, customer.getCustomerId());
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    customer.setBusiness(true);
                    customer.setTaxId(rs.getString("tax_id"));
                    customer.setName(rs.getString("name"));
                    customer.setContactName(rs.getString("contact_name"));
                    customer.setContactTitle(rs.getString("contact_title"));
                }

                /* Check if customer is an individual */
                preparedStatement =
                        con.prepareStatement(
                                "SELECT driver_license, first_name, last_name " +
                                        "FROM individual\n" +
                                        "WHERE customer_id = ?");
                preparedStatement.setLong(1, customer.getCustomerId());
                rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    customer.setBusiness(false);
                    customer.setDriverLicense(rs.getString("driver_license"));
                    customer.setFirstName(rs.getString("first_name"));
                    customer.setLastName(rs.getString("last_name"));
                }

                customers.add(customer);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return customers;
    }


    public boolean saveCustomer(Customer customer) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();


            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT max(customer_id) FROM customer");
            ResultSet resultSet = preparedStatement.executeQuery();

            long customerId = 0;

            if (resultSet.next()) {
                customerId = resultSet.getLong("max") + 1;
            }

                 preparedStatement =
                    con.prepareStatement(
                            "INSERT INTO customer (customer_id, email, phone, street, city, state, zip_code)" +
                                    " VALUES ( ?, ?, ?, ?, ?, ?, ? );"
                    );

            preparedStatement.setLong(1, customerId);
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getStreet());
            preparedStatement.setString(5, customer.getCity());
            preparedStatement.setString(6, customer.getState());
            preparedStatement.setString(7, customer.getZipCode());

            numInsertions = preparedStatement.executeUpdate();

            if (numInsertions == 0) {
                return false;
            }


            if(customer.isBusiness()){
                preparedStatement =
                        con.prepareStatement(
                                "INSERT INTO business (tax_id, customer_id, name, contact_name, contact_title)" +
                                        " VALUES ( ?, ?, ?, ?, ? );"
                        );

                preparedStatement.setString(1, customer.getTaxId());
                preparedStatement.setLong(2, customerId);
                preparedStatement.setString(3, customer.getName());
                preparedStatement.setString(4, customer.getContactName());
                preparedStatement.setString(5, customer.getContactTitle());

                numInsertions = preparedStatement.executeUpdate();

                if (numInsertions == 0) {
                    return false;
                }
            }

            else{
                preparedStatement =
                        con.prepareStatement(
                                "INSERT INTO individual (driver_license, customer_id, first_name, last_name)" +
                                        " VALUES ( ?, ?, ?, ? );"
                        );

                preparedStatement.setString(1, customer.getDriverLicense());
                preparedStatement.setLong(2, customerId);
                preparedStatement.setString(3, customer.getFirstName());
                preparedStatement.setString(4, customer.getLastName());

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