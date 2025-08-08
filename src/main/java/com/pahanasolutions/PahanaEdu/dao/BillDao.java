package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BillDao {

    public int createBill(Bill bill) {
        String insertBillSQL = "INSERT INTO bills (customer_id, total_amount, units_consumed) VALUES (?, 0, ?)";
        String insertBillItemSQL = "INSERT INTO bill_items (bill_id, item_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        int billId = -1;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement billPstmt = conn.prepareStatement(insertBillSQL, Statement.RETURN_GENERATED_KEYS)) {
                billPstmt.setInt(1, bill.getCustomerId());
                billPstmt.setInt(2, bill.getUnitsConsumed());
                billPstmt.executeUpdate();

                try (ResultSet generatedKeys = billPstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        billId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating bill failed, no ID was generated.");
                    }
                }
            }

            List<BillItem> billItems = bill.getBillItems();
            if (billItems != null && !billItems.isEmpty()) {
                try (PreparedStatement itemPstmt = conn.prepareStatement(insertBillItemSQL)) {
                    for (BillItem item : billItems) {
                        itemPstmt.setInt(1, billId);
                        itemPstmt.setInt(2, item.getItemId());
                        itemPstmt.setInt(3, item.getQuantity());
                        itemPstmt.setBigDecimal(4, item.getPriceAtPurchase());
                        itemPstmt.addBatch();
                    }
                    itemPstmt.executeBatch();
                }
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            System.err.println("Transaction failed. Rolling back changes. Error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during transaction rollback: " + ex.getMessage());
                }
            }
            billId = -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        return billId;
    }

    public Bill getBillById(int billId) {
        String sql = "SELECT b.bill_id, b.customer_id, b.bill_date, b.total_amount, b.units_consumed, " +
                "c.account_number, c.name, c.address, c.telephone, c.registered_date, " +
                "bi.bill_item_id, bi.item_id, bi.quantity, bi.price_at_purchase, " +
                "i.item_code, i.description " +
                "FROM bills b " +
                "JOIN customers c ON b.customer_id = c.customer_id " +
                "LEFT JOIN bill_items bi ON b.bill_id = bi.bill_id " +
                "LEFT JOIN items i ON bi.item_id = i.item_id " +
                "WHERE b.bill_id = ? ORDER BY i.description";

        Bill bill = null;
        List<BillItem> billItems = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (bill == null) { // Initialize Bill and Customer on the first row
                    bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setCustomerId(rs.getInt("customer_id"));
                    bill.setBillDate(rs.getTimestamp("bill_date"));
                    bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                    bill.setUnitsConsumed(rs.getInt("units_consumed"));

                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setAccountNumber(rs.getString("account_number"));
                    customer.setName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setTelephone(rs.getString("telephone"));
                    customer.setRegisteredDate(rs.getTimestamp("registered_date"));
                    bill.setCustomer(customer);
                }

                if (rs.getInt("bill_item_id") != 0) { // Process each line item
                    BillItem billItem = new BillItem();
                    billItem.setBillItemId(rs.getInt("bill_item_id"));
                    billItem.setBillId(rs.getInt("bill_id"));
                    billItem.setItemId(rs.getInt("item_id"));
                    billItem.setQuantity(rs.getInt("quantity"));
                    billItem.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));

                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemCode(rs.getString("item_code"));
                    item.setDescription(rs.getString("description"));
                    billItem.setItem(item);

                    billItems.add(billItem);
                }
            }
            if (bill != null) {
                bill.setBillItems(billItems);
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception when getting bill by ID: " + e.getMessage());
            e.printStackTrace();
            return null; // Return null on error
        }
        return bill;
    }
}
