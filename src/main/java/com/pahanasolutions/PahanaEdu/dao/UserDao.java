package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.User;
import com.pahanasolutions.PahanaEdu.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                if (PasswordUtil.verifyPassword(password, storedHash)) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
        }
        return user;
    }
}