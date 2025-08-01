package com.pahanasolutions.PahanaEdu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the MySQL database.
 * This is a basic connection manager. For a production environment,
 * using a connection pool (like HikariCP or Apache DBCP) is highly recommended
 * to improve performance and scalability.
 */
public class DatabaseConnection {

    // --- Database Connection Details ---
    // Replace with your actual database URL, user, and password.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pahana_edu_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; // <-- IMPORTANT: Change this
    private static final String DB_PASSWORD = "123456"; // <-- IMPORTANT: Change this

    /**
     * Static block to load the MySQL JDBC driver once when the class is loaded.
     */
    static {
        try {
            // The new driver class is com.mysql.cj.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // This would be a critical, unrecoverable error.
            throw new IllegalStateException("Failed to load MySQL JDBC driver", e);
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return A Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}