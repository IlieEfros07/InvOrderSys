package com.app.invordersys.controllers;


import com.app.invordersys.models.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateCustomerController {

    @FXML
    public TextField nameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public TextField addressField;

    @FXML
    public Button updateBtn;

    @FXML
    public void initialize(){
        updateBtn.setOnAction(e-> handleUpdate());
    }
    private CustomerController mainController;
    private Customer customer;

    public void setMainController(CustomerController controller) {
        this.mainController = controller;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        nameField.setText(customer.getName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        addressField.setText(customer.getAddress());
    }

    @FXML
    public void handleUpdate() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (name == null || name.trim().isEmpty()) {
            showAlert("Validation Error", "Name is required.");
            return;
        }

        String sql = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, customer.getId());
            stmt.executeUpdate();

            showAlert("Success", "Customer updated successfully!");
            mainController.showCustomers();

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not update customer.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}



