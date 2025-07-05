package com.app.invordersys.models;

import java.math.BigDecimal;

public class OrderItem {
    private int productId;
    private int quantity;
    private BigDecimal price;

    public OrderItem(int productId, int quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
}
