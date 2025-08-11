package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerDaoTest {

    private CustomerDao customerDao;
    private MockedStatic<DatabaseConnection> mockedDbConnection;

    @BeforeEach
    void setUp() {
        customerDao = new CustomerDao();
        mockedDbConnection = Mockito.mockStatic(DatabaseConnection.class);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testAddCustomerSuccess() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        CallableStatement mockCstmt = mock(CallableStatement.class);
        Customer customer = new Customer();
        customer.setAccountNumber("C001");
        customer.setName("test user");
        customer.setAddress("Colombo");
        customer.setTelephone("0123456789");

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareCall(anyString())).thenReturn(mockCstmt);
        when(mockCstmt.executeUpdate()).thenReturn(1);

        // Act
        boolean result = customerDao.addCustomer(customer);

        // Assert
        assertTrue(result);
        verify(mockCstmt).setString(1, "C001");
        verify(mockCstmt).setString(2, "test user");
    }

    @Test
    void testGetCustomerById() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPstmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("customer_id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("test user");

        // Act
        Customer customer = customerDao.getCustomerById(1);

        // Assert
        assertNotNull(customer);
        assertEquals("test user", customer.getName());
        verify(mockPstmt).setInt(1, 1);
    }

    @Test
    void testUpdateCustomerSuccess() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPstmt = mock(PreparedStatement.class);
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setName("Updated Name");

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeUpdate()).thenReturn(1);

        // Act
        boolean result = customerDao.updateCustomer(customer);

        // Assert
        assertTrue(result);
        verify(mockPstmt).setString(2, "Updated Name");
        verify(mockPstmt).setInt(5, 1);
    }

    @Test
    void testDeleteCustomerSuccess() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPstmt = mock(PreparedStatement.class);

        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeUpdate()).thenReturn(1);

        // Act
        boolean result = customerDao.deleteCustomer(1);

        // Assert
        assertTrue(result);
        verify(mockPstmt).setInt(1, 1);
    }
}