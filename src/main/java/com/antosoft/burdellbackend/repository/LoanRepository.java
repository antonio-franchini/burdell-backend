package com.antosoft.burdellbackend.repository;

import com.antosoft.burdellbackend.dto.Customer;
import com.antosoft.burdellbackend.dto.Loan;
import com.antosoft.burdellbackend.dto.LoanIncome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@Repository
public class LoanRepository {

    @Autowired
    DataSource dataSource;


    public Loan getLoan(String vin) {
        Loan loan = null;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "SELECT start_month, term, payment, interest, down_payment " +
                                    "FROM loan WHERE vin = ?");
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                loan = new Loan();
                loan.setStartMonth(resultSet.getDate("start_month"));
                loan.setTerm(resultSet.getLong("term"));
                loan.setPayment(resultSet.getLong("payment"));
                loan.setInterest(resultSet.getLong("interest"));
                loan.setDownPayment(resultSet.getLong("down_payment"));
                loan.setCustomerId(resultSet.getLong("interest"));
                loan.setVin(resultSet.getString("down_payment"));
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("" + e);
            return null;
        }
        return loan;
    }

    public List<LoanIncome> getLoanIncomeReport() {
        List<LoanIncome> report = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            for (int i = 0; i < 12; i++) {
                String monthStatement = "date_trunc('month', CURRENT_DATE - INTERVAL '" + i + " months')";
                PreparedStatement preparedStatement =
                        con.prepareStatement(
                                "SELECT EXTRACT(YEAR FROM " + monthStatement + ") as year, EXTRACT(MONTH FROM " + monthStatement + ") as month, " +
                                        "ROUND(SUM(payment), 2) as monthly_payment_total, ROUND(sum(payment)/100, 2) as monthly_share " +
                                        "FROM LOAN " +
                                        "WHERE date_trunc('month', start_month) < " + monthStatement + " AND " +
                                        "date_trunc('month', start_month) + (term || ' month')::INTERVAL >= " + monthStatement + " AND " +
                                        "date_trunc('month', start_month) > date_trunc('month', CURRENT_DATE) - INTERVAL '1 year';"
                        );
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    LoanIncome loanIncome = new LoanIncome();
                    loanIncome.setYear(resultSet.getInt("year"));
                    loanIncome.setMonth(resultSet.getInt("month"));
                    loanIncome.setMonthlyPaymentTotal(resultSet.getFloat("monthly_payment_total"));
                    loanIncome.setMonthlyShare(resultSet.getFloat("monthly_share"));
                    report.add(loanIncome);
                }
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("" + e);
            return Collections.emptyList();
        }
        return report;
    }


    public boolean addLoan(Loan loan) {

        int numInsertions = 0;

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    con.prepareStatement(
                            "INSERT INTO loan (start_month, term, payment, interest, down_payment, customer_id, vin)" +
                                    " VALUES ( ?, ?, ?, ?, ?, ?, ? );"
                    );

            preparedStatement.setDate(1, loan.getStartMonth());
            preparedStatement.setLong(2, loan.getTerm());
            preparedStatement.setLong(3, loan.getPayment());
            preparedStatement.setLong(4, loan.getInterest());
            preparedStatement.setLong(5, loan.getDownPayment());
            preparedStatement.setLong(6, loan.getCustomerId());
            preparedStatement.setString(7, loan.getVin());

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