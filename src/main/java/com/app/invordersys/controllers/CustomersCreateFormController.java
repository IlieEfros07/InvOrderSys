package com.app.invordersys.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;



public class CustomersCreateFormController {


    @FXML
    public TextField nameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public TextField addressField;

    private CustomerController mainController;
    public void setMainController(CustomerController controller)
    {
        this.mainController=controller;
    }

    @FXML
    public void handleSubmit() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (name == null || name.trim().isEmpty()) {
            showAlert("Validation Error", "Name is required.");
            return;
        }

        String sql = "INSERT INTO customers(name, email, phone, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.executeUpdate();

            showAlert("Success", "Customer added successfully!");

            nameField.clear();
            emailField.clear();
            phoneField.clear();
            addressField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not add customer.");
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
