package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (item_code, description, unit_price, stock_quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemCode());
            pstmt.setString(2, item.getDescription());
            pstmt.setBigDecimal(3, item.getUnitPrice());
            pstmt.setInt(4, item.getStockQuantity());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Exception when adding item: " + e.getMessage());
            return false;
        }
    }

    public Item getItemById(int itemId) {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception when getting item by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Item> getAllItems() {
        String sql = "SELECT * FROM items ORDER BY description";
        List<Item> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception when getting all items: " + e.getMessage());
        }
        return items;
    }

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_code = ?, description = ?, unit_price = ?, stock_quantity = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemCode());
            pstmt.setString(2, item.getDescription());
            pstmt.setBigDecimal(3, item.getUnitPrice());
            pstmt.setInt(4, item.getStockQuantity());
            pstmt.setInt(5, item.getItemId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception when updating item: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Exception when deleting item: " + e.getMessage());
            return false;
        }
    }

    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemId(rs.getInt("item_id"));
        item.setItemCode(rs.getString("item_code"));
        item.setDescription(rs.getString("description"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setStockQuantity(rs.getInt("stock_quantity"));
        return item;
    }
}