package com.app.invordersys.controllers;

import com.app.invordersys.models.Customer;
import com.app.invordersys.models.OrderItem;
import com.app.invordersys.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderCreateController {

    @FXML
    private ComboBox<Customer> customerField;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private ComboBox<Product> productField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField priceField;

    @FXML
    private ListView<String> orderItemsList;

    private final List<OrderItem> orderItems = new ArrayList<>();

    @FXML
    public void initialize() {
        loadCustomers();
        loadProducts();
        statusField.setItems(FXCollections.observableArrayList("pending", "shipped", "delivered", "canceled"));
    }

    @FXML
    public void handleAddItem() {
        Product selectedProduct = productField.getValue();
        int quantity = Integer.parseInt(quantityField.getText());
        BigDecimal price = new BigDecimal(priceField.getText());

        orderItems.add(new OrderItem(selectedProduct.getProductId(), quantity, price));
        orderItemsList.getItems().add(
                selectedProduct.getName() + " - Qty: " + quantity + " - $" + price
        );

        quantityField.clear();
        priceField.clear();
    }

    @FXML
    public void handleSubmit() {
        Customer customer = customerField.getValue();
        String status = statusField.getValue();

        if (customer == null || status == null || orderItems.isEmpty()) {
            showAlert("Validation Error", "Please complete all fields and add at least one order item.");
            return;
        }

        String insertOrderSQL = "INSERT INTO orders(customer_id, status) VALUES (?, ?) RETURNING order_id";
        String insertItemSQL = "INSERT INTO order_items(order_id, product_id, quantity, price_at_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root")) {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL);
                 PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {

                orderStmt.setInt(1, customer.getId());
                orderStmt.setString(2, status);
                ResultSet rs = orderStmt.executeQuery();
                rs.next();
                int orderId = rs.getInt("order_id");

                for (OrderItem item : orderItems) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();

                conn.commit();
                showAlert("Success", "Order and items added successfully!");
                orderItems.clear();
                orderItemsList.getItems().clear();
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                showAlert("Database Error", "Failed to insert order.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"), rs.getString("name")));
            }
            customerField.setItems(customers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"), rs.getString("name")));
            }
            productField.setItems(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
