package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class VehicleRepository {

    @Autowired
    DataSource dataSource;

    public List<Vehicle> searchVehicles(VehicleSearch vs) {

        List<Vehicle> vehicles = new ArrayList<>();
        Vehicle v = vs.getVehicle();

        String filterOutSold = null;
        String filterOutUnsold = null;
        String filterOutUnready = null;

        /* Determine if vehicles sold, unsold ready and unsold with pending parts should display
         * based on the user credentials */
        if (vs != null && vs.getPermission() != null) {
            switch (vs.getPermission()) {
                case "manager":
                case "owner":
                    filterOutSold = null;
                    filterOutUnsold = null;
                    filterOutUnready = null;
                    break;
                case "inventory clerk":
                    filterOutSold = "";
                    filterOutUnsold = null;
                    filterOutUnready = null;
                    break;
                case "salesperson":
                default:
                    filterOutSold = "";
                    filterOutUnsold = null;
                    filterOutUnready = "";
                    break;
            }
        }

        /* Only managers and owner can filter their search based on sold/unsold status */
        if (vs.getStatus() != null) {
            filterOutSold = vs.getStatus().equals(("unsold")) ? "" : null;
            filterOutUnsold = vs.getStatus().equals(("sold")) ? "" : null;
        }

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT * FROM vehicle WHERE" +
                                    " (? is null or make = ?) AND" +
                                    " (? is null or model = ?) AND" +
                                    " (? is null or ? = 0 or year = ?) AND" +
                                    " (? is null or type = ?) AND" +
                                    " (? is null or description LIKE ? or year::varchar(50) LIKE ? or make LIKE ? or model LIKE ?  ) AND" +
                                    " (? is null or vin = ?) AND" +
                                    /* Filter out sold vehicles */
                                    " (? is null or (saleperson_name is null)) AND" +
                                    /* Filter out unsold vehicles */
                                    " (? is null or (saleperson_name is not null)) AND" +
                                    /* Filter out vehicles with pending parts */
                                    " (? is null or (vin not in (select vin from part_order where status = 'ordered' or status = 'received'))) AND" +
                                    " (? is null or vin IN (SELECT vin from color_mapping where color = ?))" +
                                    " ORDER BY vin"
                    );

            preparedStatement.setString(1, v.getMake());
            preparedStatement.setString(2, v.getMake());
            preparedStatement.setString(3, v.getModel());
            preparedStatement.setString(4, v.getModel());
            preparedStatement.setLong(5, v.getYear());
            preparedStatement.setLong(6, v.getYear());
            preparedStatement.setLong(7, v.getYear());
            preparedStatement.setString(8, v.getType());
            preparedStatement.setString(9, v.getType());

            preparedStatement.setString(10, v.getDescription());
            preparedStatement.setString(11, "%"+v.getDescription()+"%");
            preparedStatement.setString(12, "%"+v.getDescription()+"%");
            preparedStatement.setString(13, "%"+v.getDescription()+"%");
            preparedStatement.setString(14, "%"+v.getDescription()+"%");

            preparedStatement.setString(15, v.getVin());
            preparedStatement.setString(16, v.getVin());

            preparedStatement.setString(17, filterOutSold);
            preparedStatement.setString(18, filterOutUnsold);
            preparedStatement.setString(19, filterOutUnready);

            preparedStatement.setString(20, v.getColors());
            preparedStatement.setString(21, v.getColors());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVin(resultSet.getString("vin"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setMake(resultSet.getString("make"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setYear(resultSet.getLong("year"));
                vehicle.setInventoryClerkName(resultSet.getString("inventory_clerk_name"));
                vehicle.setSalepersonName(resultSet.getString("saleperson_name"));
                vehicle.setMileage(resultSet.getInt("mileage"));
                vehicle.setDescription(resultSet.getString("description"));
                vehicle.setPurchasePrice(resultSet.getLong("purchase_price"));
                vehicle.setCondition(resultSet.getString("condition"));
                vehicle.setPurchaseDate(resultSet.getDate("purchase_date"));
                vehicle.setSaleDate(resultSet.getDate("sale_date"));
                vehicle.setBuyerCustomerId(resultSet.getInt("buyer_customer_id"));
                vehicle.setSellerCustomerId(resultSet.getInt("seller_customer_id"));

                PreparedStatement ps =
                        con.prepareStatement("SELECT color FROM color_mapping WHERE vin = ?");
                ps.setString(1, vehicle.getVin());
                ResultSet rs = ps.executeQuery();

                List<String> colors = new ArrayList<>();
                while (rs.next()) {
                    colors.add(rs.getString("color"));
                }

                if (!colors.isEmpty()) {
                    vehicle.setColors(String.join(", ", colors));
                }

                vehicles.add(vehicle);
            }


            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return vehicles;

    }

    public Vehicle getVehicle(String vin) {
        Vehicle vehicle = new Vehicle();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT * FROM vehicle WHERE vin = ?");
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                vehicle.setVin(resultSet.getString("vin"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setMake(resultSet.getString("make"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setYear(resultSet.getLong("year"));
                vehicle.setInventoryClerkName(resultSet.getString("inventory_clerk_name"));
                vehicle.setSalepersonName(resultSet.getString("saleperson_name"));
                vehicle.setMileage(resultSet.getInt("mileage"));
                vehicle.setDescription(resultSet.getString("description"));
                vehicle.setPurchasePrice(resultSet.getLong("purchase_price"));
                vehicle.setCondition(resultSet.getString("condition"));
                vehicle.setPurchaseDate(resultSet.getDate("purchase_date"));
                vehicle.setSaleDate(resultSet.getDate("sale_date"));
                vehicle.setBuyerCustomerId(resultSet.getInt("buyer_customer_id"));
                vehicle.setSellerCustomerId(resultSet.getInt("seller_customer_id"));
            }

            /* Get the colors */
            preparedStatement =
                    con.prepareStatement("SELECT color FROM color_mapping WHERE vin = ?");
            preparedStatement.setString(1, vin);
            resultSet = preparedStatement.executeQuery();

            List<String> colors = new ArrayList<>();
            while (resultSet.next()) {
                colors.add(resultSet.getString("color"));
            }

            if (!colors.isEmpty()) {
                vehicle.setColors(String.join(", ", colors));
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return vehicle;

    }

    public Vehicle addVehicle(Vehicle vehicle) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "INSERT INTO vehicle (vin, type, make, model, year, inventory_clerk_name, mileage," +
                                    "description, purchase_price, condition, purchase_date, seller_customer_id) " +
                                    "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );"
                    );

            preparedStatement.setString(1, vehicle.getVin());
            preparedStatement.setString(2, vehicle.getType());
            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setLong(5, vehicle.getYear());
            preparedStatement.setString(6, vehicle.getInventoryClerkName());
            preparedStatement.setLong(7, vehicle.getMileage());
            preparedStatement.setString(8, vehicle.getDescription());
            preparedStatement.setLong(9, vehicle.getPurchasePrice());
            preparedStatement.setString(10, vehicle.getCondition());
            preparedStatement.setDate(11, vehicle.getPurchaseDate());
            preparedStatement.setLong(12, vehicle.getSellerCustomerId());
            /* not populating saleperson_name, sale_date and buyer_customer_id yet */

            numInsertions = preparedStatement.executeUpdate();

            if (numInsertions == 0) {
                return null;
            }

            String[] colors = vehicle.getColors().split(",");
            preparedStatement = con.prepareStatement("INSERT INTO color_mapping (vin, color) VALUES ( ?,  ?)");

            for (int i = 0; i < colors.length; i++) {
                preparedStatement.setString(1, vehicle.getVin());
                preparedStatement.setString(2, colors[i]);
                numInsertions = preparedStatement.executeUpdate();
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return numInsertions == 0 ? null : vehicle;

    }

    public List<SaleDetail> getSaleDetailByDateReport() {
        List<SaleDetail> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT extract(year from sale_date) || '-' || to_char(sale_date, 'MM') as sale_month, COUNT(*) as vehicles_sold, " +
                                    "round(SUM(purchase_price) * 1.25, 2) as total_sales, round(SUM(purchase_price * 0.25) - SUM(COALESCE(parts_cost, 0)), 2) as net_income " +
                                    "FROM (select v.vin, v.purchase_price, v.sale_date, sum(po.cost) as parts_cost from vehicle v LEFT JOIN part_order po ON v.vin = po.vin group by v.vin) as veh " +
                                    "WHERE sale_date notnull GROUP BY sale_month ORDER BY sale_month DESC;"
                    );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SaleDetail saleDetail = new SaleDetail();
                saleDetail.setSaleMonth(resultSet.getString("sale_month"));
                saleDetail.setVehiclesSold(resultSet.getInt("vehicles_sold"));
                saleDetail.setTotalSales(resultSet.getFloat("total_sales"));
                saleDetail.setNetIncome(resultSet.getFloat("net_income"));
                report.add(saleDetail);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }

    public List<VehicleAvgDays> getVehicleAveragesDaysReport() {
        List<VehicleAvgDays> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT vehicle_type, COALESCE(cast(cast(avg_days_in_inventory as numeric(10, 2)) as VARCHAR(50)), 'N/A') AS avg_days_in_inventory " +
                                    "FROM vehicle_type VT LEFT JOIN " +
                                    "(SELECT type, AVG(sale_date-purchase_date) as avg_days_in_inventory FROM vehicle WHERE sale_date IS NOT NULL GROUP BY type) as V " +
                                    "ON VT.vehicle_type = V.type;"
                    );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                VehicleAvgDays vehicleAvgDays = new VehicleAvgDays();
                vehicleAvgDays.setType(resultSet.getString("vehicle_type"));
                vehicleAvgDays.setAvg(resultSet.getString("avg_days_in_inventory"));
                report.add(vehicleAvgDays);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }

    public List<SalepersonPerformanceReport> getSalepersonPerformanceReport(String saleMonth) {
        List<SalepersonPerformanceReport> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT first_name, last_name, COUNT(*) as vehicles_sold, ROUND(SUM(purchase_price)*1.25, 2) as total_sales " +
                                    "FROM vehicle v  JOIN profile p ON v.saleperson_name = p.username " +
                                    "WHERE sale_date notnull and extract(year from sale_date) || '-' || to_char(sale_date, 'MM') = ? " +
                                    "GROUP BY first_name, last_name ORDER BY vehicles_sold DESC, total_sales DESC;"
                    );
            preparedStatement.setString(1, saleMonth);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SalepersonPerformanceReport salepersonPerformanceReport = new SalepersonPerformanceReport();
                salepersonPerformanceReport.setSalepersonFirstName(resultSet.getString("first_name"));
                salepersonPerformanceReport.setSalepersonLastName(resultSet.getString("last_name"));
                salepersonPerformanceReport.setVehiclesSold(resultSet.getInt("vehicles_sold"));
                salepersonPerformanceReport.setTotalSales(resultSet.getFloat("total_sales"));
                report.add(salepersonPerformanceReport);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }

    public List<PricePerCondition> getPricePerConditionReport() {
        List<PricePerCondition> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT vt.vehicle_type, COALESCE(v.e, ROUND(0, 2)) as excellent, COALESCE(v.vg, ROUND(0, 2)) as very_good, COALESCE(v.g, ROUND(0, 2)) as good, COALESCE(v.f, ROUND(0, 2)) as fair " +
                                    "FROM vehicle_type vt LEFT JOIN " +
                                    "(SELECT type, ROUND(avg((case when condition = 'Excellent' then purchase_price else 0 end)), 2) as e, " +
                                    "ROUND(avg((case when condition = 'Very Good' then purchase_price else 0 end)), 2) as vg, " +
                                    "ROUND(avg((case when condition = 'Good' then purchase_price else 0 end)), 2) as g, " +
                                    "ROUND(avg((case when condition = 'Fair' then purchase_price else 0 end)), 2) as f " +
                                    "FROM vehicle GROUP BY type) as v " +
                                    "ON vt.vehicle_type = v.type;"
                    );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PricePerCondition pricePerCondition = new PricePerCondition();
                pricePerCondition.setType(resultSet.getString("vehicle_type"));
                pricePerCondition.setExcellent(resultSet.getFloat("excellent"));
                pricePerCondition.setVeryGood(resultSet.getFloat("very_good"));
                pricePerCondition.setGood(resultSet.getFloat("good"));
                pricePerCondition.setFair(resultSet.getFloat("fair"));
                report.add(pricePerCondition);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }

    public boolean addSaleInfo(SaleOrder saleOrder) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "UPDATE  vehicle SET" +
                                    " saleperson_name = ?, " +
                                    " sale_date = ?, " +
                                    " buyer_customer_id = ? " +
                                    " WHERE vin = ?"
                    );

            preparedStatement.setString(1, saleOrder.getSalepersonName());
            preparedStatement.setDate(2, saleOrder.getSaleDate());
            preparedStatement.setLong(3, saleOrder.getBuyerCustomerId());
            preparedStatement.setString(4, saleOrder.getVin());

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

    public List<VehicleType> getVehicleTypes() {
        List<VehicleType> vehicleTypes = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT vehicle_type FROM vehicle_type;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                VehicleType vehicleType = new VehicleType();

                vehicleType.setType(resultSet.getString("vehicle_type"));

                vehicleTypes.add(vehicleType);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return vehicleTypes;
    }

    public List<VehicleMake> getVehicleMakes() {
        List<VehicleMake> vehicleMakes = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT manufacturer_name FROM manufaturer;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                VehicleMake vehicleMake = new VehicleMake();

                vehicleMake.setMake(resultSet.getString("manufacturer_name"));

                vehicleMakes.add(vehicleMake);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return vehicleMakes;
    }

    public List<Color> getColors() {
        List<Color> colors = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT color FROM color;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Color color = new Color();

                color.setColor(resultSet.getString("color"));

                colors.add(color);
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return colors;
    }

}