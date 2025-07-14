package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.app.invordersys.models.RestockRequest;
import com.app.invordersys.models.Supplier;
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

public class RestockRequestController {

    @FXML
    public Button restockRequestCreate;

    @FXML
    public VBox restockRequestList;

    @FXML
    public TextField searchRestockRequest;

    @FXML
    public Button searchRestockRequestBtn;

    @FXML
    public void initialize() {
        restockRequestCreate.setOnAction(e -> openRestockRequestCreate());
        searchRestockRequestBtn.setOnAction(e -> searchRestockRequest());

        showRestockRequests();
    }

    private void openRestockRequestCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/restockRequestCreate.fxml"));
            Parent root = loader.load();

            RestockRequestCreateController controller = loader.getController();
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

    public void showRestockRequests() {
        List<RestockRequest> restockRequests = getRestockRequests("SELECT * FROM restock_requests;");
        restockRequestList.getChildren().clear();

        for (RestockRequest restockRequest : restockRequests) {
            restockRequestList.getChildren().add(createRestockRequestBox(restockRequest));
        }
    }

    private HBox createRestockRequestBox(RestockRequest restockRequest) {
        Label idLabel = new Label("ID: " + restockRequest.getRestockId());
        Label productIdLabel = new Label("Product ID: " + restockRequest.getProductId());
        Label quantityLabel = new Label("Quantity: " + restockRequest.getQuantityRequested());
        Label dateLabel = new Label("Date: " + restockRequest.getRequestedDate());
        Label statusLabel = new Label("Status: " + restockRequest.getStatus());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("update-button");
        updateBtn.setOnAction(e-> openRestockRequestUpdate(restockRequest));

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e-> deleteRestockRequest(restockRequest));

        HBox hbox = new HBox(idLabel, productIdLabel, quantityLabel, dateLabel, statusLabel,updateBtn,deleteBtn);
        hbox.getStyleClass().add("restock-requests-box");
        hbox.setSpacing(10);

        return hbox;
    }


    public void deleteRestockRequest(RestockRequest restockRequest){
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?;";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1,restockRequest.getRestockId());
            stmt.executeUpdate();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete!!");
            alert.setHeaderText("Warehouse Deleted!!");
            alert.setContentText("Warehouse Deleted Succsesfuly");
            alert.showAndWait();
            showRestockRequests();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openRestockRequestUpdate(RestockRequest restockRequest){
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/updateRestockRequest.fxml"));
            Parent root = loader.load();

            UpdateRestockRequestController controller = loader.getController();
            controller.setMainController(this);
            controller.setRestock(restockRequest);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Update Restock Requests");
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void searchRestockRequest() {
        String searchValue = searchRestockRequest.getText();

        List<RestockRequest> restockRequests = getRestockRequests(
                "SELECT * FROM restock_requests WHERE " +
                        "CAST(restock_id AS TEXT) ILIKE '%" + searchValue + "%' OR " +
                        "CAST(product_id AS TEXT) ILIKE '%" + searchValue + "%' OR " +
                        "CAST(quantity_requested AS TEXT) ILIKE '%" + searchValue + "%' OR " +
                        "status ILIKE '%" + searchValue + "%';"
        );

        restockRequestList.getChildren().clear();
        for (RestockRequest restockRequest : restockRequests) {
            restockRequestList.getChildren().add(createRestockRequestBox(restockRequest));
        }
    }

    private List<RestockRequest> getRestockRequests(String sql) {
        List<RestockRequest> restockRequests = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int restockId = rs.getInt("restock_id");
                int productId = rs.getInt("product_id");
                int quantityRequested = rs.getInt("quantity_requested");
                Timestamp requestedDate = rs.getTimestamp("requested_date");
                String status = rs.getString("status");

                restockRequests.add(new RestockRequest(restockId, productId, quantityRequested, requestedDate, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restockRequests;
    }
}