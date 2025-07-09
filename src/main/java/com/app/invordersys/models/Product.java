package com.app.invordersys.models;

import com.app.invordersys.models.Category;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String name;
    private BigDecimal price;
    private int stock;
    private Category category;
    private int categoryId;

    public Product(int productId, String name, BigDecimal price, int stock, Category category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.categoryId = (category != null) ? category.getId() : 0;
    }

    public Product(int productId, String name, BigDecimal price, int stock, int categoryId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.category = null;
    }

    public Product(int id, String name) {
        this.productId = id;
        this.name = name;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }

    public int getCategoryId() {
        return category != null ? category.getId() : categoryId;
    }

    public Category getCategory() { return category; }

    @Override
    public String toString() {
        String categoryName = (category != null) ? category.getName() : "No Category";
        return name + " (" + categoryName + ") - $" + price;
    }
}
