package com.app.invordersys.controllers;

import com.app.invordersys.models.Product;
import com.app.invordersys.models.RestockRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateRestockRequestController {

    @FXML
    public ComboBox<Product> productField;

    @FXML
    public TextField quantityField;

    @FXML
    public ComboBox<String> statusField;

    @FXML
    public Button updateBtn;

    private RestockRequestController mainController;
    private RestockRequest restockRequest;

    public void setMainController(RestockRequestController controller) {
        this.mainController = controller;
    }

    public void setRestock(RestockRequest restockRequest) {
        this.restockRequest = restockRequest;

        loadProducts();

        quantityField.setText(String.valueOf(restockRequest.getQuantityRequested()));
        statusField.setValue(restockRequest.getStatus());
    }

    @FXML
    public void initialize() {
        statusField.setItems(FXCollections.observableArrayList("pending", "completed"));
        updateBtn.setOnAction(e -> updateRestockRequest());
    }

    private void loadProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name")
                );
                products.add(product);

                if (restockRequest != null && product.getProductId() == restockRequest.getProductId()) {
                    productField.setValue(product);
                }
            }

            productField.setItems(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRestockRequest() {
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

        String sql = "UPDATE restock_requests SET product_id = ?, quantity_requested = ?, status = ? WHERE restock_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedProduct.getProductId());
            stmt.setInt(2, quantity);
            stmt.setString(3, status);
            stmt.setInt(4, restockRequest.getRestockId());

            stmt.executeUpdate();

            showAlert("Success", "Restock request updated.");
            mainController.showRestockRequests();

            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update restock request.");
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
