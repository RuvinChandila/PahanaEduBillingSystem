package com.pahanasolutions.PahanaEdu.dao;

import com.pahanasolutions.PahanaEdu.db.DatabaseConnection;
import com.pahanasolutions.PahanaEdu.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemDaoTest {

    // The DAO instance we are testing
    @InjectMocks
    private ItemDao itemDao;

    // Mocks for JDBC objects
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    // A static mock for the DatabaseConnection utility class
    private MockedStatic<DatabaseConnection> mockedDbConnection;

    @BeforeEach
    void setUp() {
        // Start mocking the static DatabaseConnection.getConnection() method
        mockedDbConnection = Mockito.mockStatic(DatabaseConnection.class);
        // When getConnection() is called anywhere, it will return our mockConnection
        mockedDbConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    private Item createSampleItem() {
        Item item = new Item();
        item.setItemId(1);
        item.setItemCode("ITM-001");
        item.setDescription("Sample Item");
        item.setUnitPrice(new BigDecimal("99.99"));
        item.setStockQuantity(50);
        return item;
    }

    @Test
    @DisplayName("addItem should return true on successful insertion")
    void addItem_Success() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        boolean result = itemDao.addItem(createSampleItem());

        // Assert
        assertTrue(result, "addItem should return true when the database operation is successful.");
        verify(mockPreparedStatement).setString(1, "ITM-001");
        verify(mockPreparedStatement).setString(2, "Sample Item");
        verify(mockPreparedStatement).setBigDecimal(3, new BigDecimal("99.99"));
        verify(mockPreparedStatement).setInt(4, 50);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    @DisplayName("addItem should return false when a SQLException occurs")
    void addItem_Failure_SQLException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database connection failed"));

        // Act
        boolean result = itemDao.addItem(createSampleItem());

        // Assert
        assertFalse(result, "addItem should return false upon a SQLException.");
    }

    @Test
    @DisplayName("getItemById should return an Item when found")
    void getItemById_Found() throws SQLException {
        // Arrange
        Item expectedItem = createSampleItem();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("item_id")).thenReturn(expectedItem.getItemId());
        when(mockResultSet.getString("item_code")).thenReturn(expectedItem.getItemCode());
        when(mockResultSet.getString("description")).thenReturn(expectedItem.getDescription());
        when(mockResultSet.getBigDecimal("unit_price")).thenReturn(expectedItem.getUnitPrice());
        when(mockResultSet.getInt("stock_quantity")).thenReturn(expectedItem.getStockQuantity());


        // Act
        Item actualItem = itemDao.getItemById(1);

        // Assert
        assertNotNull(actualItem, "The returned item should not be null.");
        assertEquals(expectedItem.getDescription(), actualItem.getDescription(), "The item description should match.");
        verify(mockPreparedStatement).setInt(1, 1);
    }

    @Test
    @DisplayName("getItemById should return null when Item is not found")
    void getItemById_NotFound() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no record found

        // Act
        Item result = itemDao.getItemById(99);

        // Assert
        assertNull(result, "Should return null when no item is found for the given ID.");
    }

    @Test
    @DisplayName("getAllItems should return a list of items")
    void getAllItems_Success() throws SQLException {
        // Arrange
        Item sampleItem = createSampleItem();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one record

        // Stub all columns that mapResultSetToItem reads
        when(mockResultSet.getInt("item_id")).thenReturn(sampleItem.getItemId());
        when(mockResultSet.getString("item_code")).thenReturn(sampleItem.getItemCode());
        when(mockResultSet.getString("description")).thenReturn(sampleItem.getDescription());
        when(mockResultSet.getBigDecimal("unit_price")).thenReturn(sampleItem.getUnitPrice());
        when(mockResultSet.getInt("stock_quantity")).thenReturn(sampleItem.getStockQuantity());

        // Act
        List<Item> items = itemDao.getAllItems();

        // Assert
        assertFalse(items.isEmpty(), "The list of items should not be empty.");
        assertEquals(1, items.size(), "The list should contain one item.");
        assertEquals("Sample Item", items.get(0).getDescription(), "The item description should match the sample.");
        assertEquals(new BigDecimal("99.99"), items.get(0).getUnitPrice(), "The item unit price should match the sample.");
    }

    @Test
    @DisplayName("getAllItems should return an empty list when no items exist")
    void getAllItems_Empty() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no records

        // Act
        List<Item> items = itemDao.getAllItems();

        // Assert
        assertTrue(items.isEmpty(), "The list of items should be empty.");
    }

    @Test
    @DisplayName("updateItem should return true on successful update")
    void updateItem_Success() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate 1 row affected

        // Act
        boolean result = itemDao.updateItem(createSampleItem());

        // Assert
        assertTrue(result, "updateItem should return true for a successful update.");
        verify(mockPreparedStatement).setInt(5, 1); // Verify the WHERE clause parameter
    }

    @Test
    @DisplayName("updateItem should return false when no rows are updated")
    void updateItem_Failure_NoRowsAffected() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate 0 rows affected

        // Act
        boolean result = itemDao.updateItem(createSampleItem());

        // Assert
        assertFalse(result, "updateItem should return false if no rows were affected.");
    }

    @Test
    @DisplayName("deleteItem should return true on successful deletion")
    void deleteItem_Success() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate 1 row affected

        // Act
        boolean result = itemDao.deleteItem(1);

        // Assert
        assertTrue(result, "deleteItem should return true for a successful deletion.");
        verify(mockPreparedStatement).setInt(1, 1);
    }

    @Test
    @DisplayName("deleteItem should return false when no rows are deleted")
    void deleteItem_Failure_NoRowsAffected() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate 0 rows affected

        // Act
        boolean result = itemDao.deleteItem(99);

        // Assert
        assertFalse(result, "deleteItem should return false if no rows were affected.");
    }
}
