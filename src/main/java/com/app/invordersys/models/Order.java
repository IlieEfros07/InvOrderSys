package com.app.invordersys.models;

public class Order {
    private int id;
    private String customerName;
    private String status;

    public Order(int id, String customerName, String status) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
}
