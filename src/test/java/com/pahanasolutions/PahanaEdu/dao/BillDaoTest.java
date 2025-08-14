package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.Bill;
import com.pahanasolutions.PahanaEdu.model.BillItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BillDaoTest {

    private BillDao billDao;
    private MockedStatic<DatabaseConnection> mockedDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockBillPstmt;
    private PreparedStatement mockItemPstmt;
    private ResultSet mockGeneratedKeys;

    @BeforeEach
    void setUp() throws SQLException {
        billDao = new BillDao();
        mockedDbConnection = Mockito.mockStatic(DatabaseConnection.class);

        // Mocks
        mockConnection = mock(Connection.class);
        mockBillPstmt = mock(PreparedStatement.class);
        mockItemPstmt = mock(PreparedStatement.class);
        mockGeneratedKeys = mock(ResultSet.class);

        // General mocking behavior
        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(contains("bills"), anyInt())).thenReturn(mockBillPstmt);
        when(mockConnection.prepareStatement(contains("bill_items"))).thenReturn(mockItemPstmt);
        when(mockBillPstmt.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testCreateBillSuccess() throws Exception {
        // Arrange
        Bill bill = new Bill();
        bill.setCustomerId(1);
        bill.setUnitsConsumed(150);
        BillItem item = new BillItem();
        item.setItemId(1);
        item.setQuantity(2);
        item.setPriceAtPurchase(new BigDecimal("10.00"));
        bill.setBillItems(Collections.singletonList(item));

        // Mocking the sequence
        when(mockBillPstmt.executeUpdate()).thenReturn(1);
        when(mockGeneratedKeys.next()).thenReturn(true);
        when(mockGeneratedKeys.getInt(1)).thenReturn(101); // Mock generated bill_id
        when(mockItemPstmt.executeBatch()).thenReturn(new int[]{1});

        // Act
        int billId = billDao.createBill(bill);

        // Assert
        assertEquals(101, billId);
        // Verify transaction management
        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).commit();
        verify(mockConnection).setAutoCommit(true);
        verify(mockConnection).close();
        verify(mockConnection, never()).rollback();
    }

    @Test
    void testCreateBillRollbackOnFailure() throws Exception {
        // Arrange
        Bill bill = new Bill();
        bill.setCustomerId(1);

        // Mocking a failure during bill insertion
        when(mockBillPstmt.executeUpdate()).thenThrow(new SQLException("DB Error"));

        // Act
        int billId = billDao.createBill(bill);

        // Assert
        assertEquals(-1, billId);
        // Verify transaction rollback
        verify(mockConnection).setAutoCommit(false);
        verify(mockConnection).rollback();
        verify(mockConnection, never()).commit();
        verify(mockConnection).setAutoCommit(true);
        verify(mockConnection).close();
    }
}