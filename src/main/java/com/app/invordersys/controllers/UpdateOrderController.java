package com.app.invordersys.controllers;

import com.app.invordersys.models.Customer;
import com.app.invordersys.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateOrderController {

    @FXML
    private ComboBox<Customer> customerField;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private Button updateBtn;

    private OrderController mainController;
    private Order order;

    public void setMainController(OrderController controller) {
        this.mainController = controller;
    }

    public void setOrder(Order order) {
        this.order = order;
        loadCustomers();

        statusField.setItems(FXCollections.observableArrayList("pending", "shipped", "delivered", "canceled"));
        statusField.setValue(order.getStatus());
    }

    @FXML
    public void initialize() {
        updateBtn.setOnAction(e -> updateOrder());
    }

    private void loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("customer_id"), rs.getString("name"));
                customers.add(customer);

            }

            customerField.setItems(customers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOrder() {
        Customer selectedCustomer = customerField.getValue();
        String status = statusField.getValue();

        if (selectedCustomer == null || status == null) {
            showAlert("Validation Error", "All fields are required.");
            return;
        }

        String sql = "UPDATE orders SET customer_id = ?, status = ? WHERE order_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedCustomer.getId());
            stmt.setString(2, status);
            stmt.setInt(3, order.getId());

            stmt.executeUpdate();

            showAlert("Success", "Order updated successfully.");
            mainController.showOrders();

            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update order.");
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
