package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.app.invordersys.models.Customer;
import com.app.invordersys.models.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerController {

    @FXML
    public Button customerCreate;

    @FXML
    public VBox customerList;

    @FXML
    public TextField searchCustomer;

    @FXML
    public Button searchCustomerBtn;

    @FXML
    public void initialize() {
        customerCreate.setOnAction(e -> openCustomerCreate());
        searchCustomerBtn.setOnAction(e -> searchCustomer());

        showCustomers();
    }

    private void openCustomerCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/customersCreate.fxml"));
            Parent root = loader.load();

            CustomersCreateFormController controller = loader.getController();
            controller.setMainController(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Add Order");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomers() {
        List<Customer> customers = getCustomers("SELECT * FROM customers;");
        customerList.getChildren().clear();

        for (Customer customer : customers) {
            customerList.getChildren().add(createCustomerBox(customer));
        }
    }

    private HBox createCustomerBox(Customer customer) {
        Label nameLabel = new Label(customer.getName());
        Label emailLabel = new Label(customer.getEmail());
        Label phoneLabel = new Label(customer.getPhone());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("update-button");
        updateBtn.setOnAction(e -> openCustomerUpdate(customer));

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e-> deleteCustomer(customer));

        HBox hbox = new HBox(nameLabel, emailLabel, phoneLabel,updateBtn,deleteBtn);
        hbox.getStyleClass().add("customers-box");
        hbox.setSpacing(10);

        return hbox;
    }

    private void openCustomerUpdate(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/updateCustomer.fxml"));
            Parent root = loader.load();

            UpdateCustomerController controller = loader.getController();
            controller.setMainController(this);
            controller.setCustomer(customer);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Update Customer");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteCustomer(Customer customer) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getId());
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Deleted");
            alert.setHeaderText(null);
            alert.setContentText("Customer successfully deleted.");
            alert.showAndWait();

            showCustomers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void searchCustomer() {
        String searchValue = searchCustomer.getText();

        List<Customer> customers = getCustomers(
                "SELECT * FROM customers WHERE " +
                        "name ILIKE '%" + searchValue + "%' OR " +
                        "email ILIKE '%" + searchValue + "%' OR " +
                        "phone ILIKE '%" + searchValue + "%';"
        );

        customerList.getChildren().clear();
        for (Customer customer : customers) {
            customerList.getChildren().add(createCustomerBox(customer));
        }
    }

    private List<Customer> getCustomers(String sql) {
        List<Customer> customers = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                customers.add(new Customer(id, name, email, phone, address));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}
