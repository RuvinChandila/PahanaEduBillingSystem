package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.User;
import com.pahanasolutions.PahanaEdu.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserDaoTest {

    private UserDao userDao;
    private MockedStatic<DatabaseConnection> mockedDbConnection;
    private MockedStatic<PasswordUtil> mockedPasswordUtil;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        mockedDbConnection = Mockito.mockStatic(DatabaseConnection.class);
        mockedPasswordUtil = Mockito.mockStatic(PasswordUtil.class);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
        mockedPasswordUtil.close();
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPstmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password_hash")).thenReturn("hashed_password");

        // Mock password verification
        when(PasswordUtil.verifyPassword("password123", "hashed_password")).thenReturn(true);

        // Act
        User user = userDao.login("testuser", "password123");

        // Assert
        assertNotNull(user);
        verify(mockPstmt).setString(1, "testuser");
    }

    @Test
    void testLoginFailed() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPstmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No user found

        // Act
        User user = userDao.login("wronguser", "password");

        // Assert
        assertNull(user);
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPstmt = mock(PreparedStatement.class);
        User newUser = new User();
        newUser.setFullName("Test User");
        newUser.setUsername("newuser");
        newUser.setPasswordHash("new_hash");

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeUpdate()).thenReturn(1); // 1 row affected

        // Act
        boolean result = userDao.registerUser(newUser);

        // Assert
        assertTrue(result);
        verify(mockPstmt).setString(1, "Test User");
        verify(mockPstmt).setString(2, "newuser");
        verify(mockPstmt).setString(3, "new_hash");
    }
}