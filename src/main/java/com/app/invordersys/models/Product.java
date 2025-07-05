package com.app.invordersys.models;

public class Product {

    private int product_id;

    private String name;

    private int categoryId;

    private double price;

    private int stock;

    public Product(int product_id, String name, int categoryId,double price, int stock){
        this.product_id=product_id;
        this.name=name;
        this.categoryId=categoryId;
        this.price=price;
        this.stock=stock;
    }

    public Product(int productId, String name) {
    }

    public int getProductId() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }


    @Override
    public String toString(){
        return this.name + " -$ "+ this.price;
    }


}
