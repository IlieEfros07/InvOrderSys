package com.app.invordersys.models;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    // Full constructor
    public Customer(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }


    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
        this.email = null;
        this.phone = null;
        this.address = null;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return name;
    }
}