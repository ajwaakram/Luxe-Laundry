package com.find.luxelaundary.Model;


import java.io.Serializable;
import java.util.Map;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private String date;
    private String phoneNumber;
    private String address;
    private String totalBill;
    private String status; // Pending, Completed, Canceled
    private Map<String, Object> cartItems;

    public Order() {}

    public Order(String orderId, String userId, String date, String phoneNumber,
                 String address, String totalBill, String status, Map<String, Object> cartItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalBill = totalBill;
        this.status = status;
        this.cartItems = cartItems;
    }

    // Getters and setters for all fields

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTotalBill() { return totalBill; }
    public void setTotalBill(String totalBill) { this.totalBill = totalBill; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Map<String, Object> getCartItems() { return cartItems; }
    public void setCartItems(Map<String, Object> cartItems) { this.cartItems = cartItems; }
}
