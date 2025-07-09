package com.app.invordersys.controllers;

import com.app.invordersys.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestockRequestCreateController {

    @FXML
    public ComboBox<Product> productField;

    @FXML
    public TextField quantityField;

    @FXML
    public ComboBox<String> statusField;

    private RestockRequestController mainController;
    public void setMainController(RestockRequestController controller)
    {
        this.mainController=controller;
    }

    @FXML
    public void initialize() {
        loadProducts();
        statusField.setItems(FXCollections.observableArrayList("pending", "completed"));
    }

    @FXML
    public void handleSubmit() {
        Product selectedProduct = productField.getValue();
        String quantityText = quantityField.getText();
        String status = statusField.getValue();

        if (selectedProduct == null || quantityText == null || status == null) {
            showAlert("Validation Error", "All fields are required.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity must be a positive integer.");
            return;
        }

        String sql = "INSERT INTO restock_requests(product_id, quantity_requested, status) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedProduct.getProductId());
            stmt.setInt(2, quantity);
            stmt.setString(3, status);
            stmt.executeUpdate();

            showAlert("Success", "Restock request submitted.");
            quantityField.clear();
            productField.setValue(null);
            statusField.setValue(null);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to submit restock request.");
        }
    }

    private void loadProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name")
                ));
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
