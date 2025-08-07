package com.pahanasolutions.PahanaEdu.model;

import java.math.BigDecimal;

public class BillItem {
    private int billItemId;
    private int billId;
    private int itemId;
    private int quantity;
    private BigDecimal priceAtPurchase;

    // Constructors, Getters, and Setters
    public BillItem() {}

    public int getBillItemId() { return billItemId; }
    public void setBillItemId(int billItemId) { this.billItemId = billItemId; }
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    private Item item;

    // Add getter and setter
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}