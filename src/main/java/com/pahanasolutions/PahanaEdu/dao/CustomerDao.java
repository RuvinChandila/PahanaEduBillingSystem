package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDao {


    public boolean addCustomer(Customer customer) {
        // Calling the stored procedure `AddCustomer`
        String sql = "{CALL AddCustomer(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, customer.getAccountNumber());
            cstmt.setString(2, customer.getName());
            cstmt.setString(3, customer.getAddress());
            cstmt.setString(4, customer.getTelephone());

            cstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("SQL Exception when adding customer: " + e.getMessage());
            return false;
        }
    }


    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        Customer customer = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                customer = mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception when getting customer by ID: " + e.getMessage());
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM customers ORDER BY name";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception when getting all customers: " + e.getMessage());
        }
        return customers;
    }


    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET account_number = ?, name = ?, address = ?, telephone = ? WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getAccountNumber());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getTelephone());
            pstmt.setInt(5, customer.getCustomerId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception when updating customer: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Catches integrity constraint violations (e.g., trying to delete a customer with bills)
            System.err.println("SQL Exception when deleting customer: " + e.getMessage());
            return false;
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setAccountNumber(rs.getString("account_number"));
        customer.setName(rs.getString("name"));
        customer.setAddress(rs.getString("address"));
        customer.setTelephone(rs.getString("telephone"));
        customer.setRegisteredDate(rs.getTimestamp("registered_date"));
        return customer;
    }
}