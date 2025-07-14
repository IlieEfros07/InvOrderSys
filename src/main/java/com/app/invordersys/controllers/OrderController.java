package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
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

public class OrderController {

    @FXML
    public Button orderCreate;

    @FXML
    public VBox orderList;

    @FXML
    public TextField searchOrder;

    @FXML
    public Button searchOrderBtn;

    @FXML
    public void initialize() {
        orderCreate.setOnAction(e -> openOrderCreate());
        searchOrderBtn.setOnAction(e -> searchOrders());
        showOrders();
    }

    private void openOrderCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/orderCreate.fxml"));
            Parent root = loader.load();

            OrderCreateController controller = loader.getController();
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

    public void showOrders() {
        List<Order> orders = getOrders("SELECT o.order_id, c.name AS customer_name, o.status " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.customer_id;");
        orderList.getChildren().clear();
        for (Order order : orders) {
            orderList.getChildren().add(createOrderBox(order));
        }
    }

    private HBox createOrderBox(Order order) {
        Label idLabel = new Label("Order #" + order.getId());
        Label customerLabel = new Label(order.getCustomerName());
        Label statusLabel = new Label(order.getStatus());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("update-button");
        updateBtn.setOnAction(e-> openOrderUpdate(order));

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e-> deleteOrder(order));

        HBox hbox = new HBox(idLabel, customerLabel, statusLabel,updateBtn,deleteBtn);
        hbox.getStyleClass().add("order-box");
        hbox.setSpacing(10);
        return hbox;
    }

    public void openOrderUpdate(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/updateOrder.fxml"));
            Parent root = loader.load();

            UpdateOrderController controller = loader.getController();
            controller.setMainController(this);
            controller.setOrder(order);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Update Order");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(Order order) {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getId());
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Deleted");
            alert.setHeaderText(null);
            alert.setContentText("Order successfully deleted.");
            alert.showAndWait();

            showOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void searchOrders() {
        String searchValue = searchOrder.getText().trim();

        String sql = "SELECT o.order_id, c.name AS customer_name, o.status " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.customer_id " +
                "WHERE CAST(o.order_id AS TEXT) ILIKE ? OR c.name ILIKE ? OR o.status ILIKE ?";

        List<Order> orders = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String likeValue = "%" + searchValue + "%";
            pstmt.setString(1, likeValue);
            pstmt.setString(2, likeValue);
            pstmt.setString(3, likeValue);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        orderList.getChildren().clear();
        for (Order order : orders) {
            orderList.getChildren().add(createOrderBox(order));
        }
    }

    private List<Order> getOrders(String sql) {
        List<Order> orders = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
}
