package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.app.invordersys.models.Warehouse;
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

public class WarehouseController {

    @FXML
    public Button warehouseCreate;

    @FXML
    public VBox warehouseList;

    @FXML
    public TextField searchWarehouse;

    @FXML
    public Button searchWarehouseBtn;

    @FXML
    public void initialize() {
        warehouseCreate.setOnAction(e -> openWarehouseCreate());
        searchWarehouseBtn.setOnAction(e -> searchWarehouse());

        showWarehouses();
    }

    private void openWarehouseCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/warehouseCreate.fxml"));
            Parent root = loader.load();


            WarehouseCreateController controller = loader.getController();
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

    public void showWarehouses() {
        List<Warehouse> warehouses = getWarehouses("SELECT * FROM warehouse_locations;");
        warehouseList.getChildren().clear();

        for (Warehouse warehouse : warehouses) {
            warehouseList.getChildren().add(createWarehouseBox(warehouse));
        }
    }

    private HBox createWarehouseBox(Warehouse warehouse) {
        Label nameLabel = new Label(warehouse.getName());
        Label locationLabel = new Label(warehouse.getLocation());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("update-button");
        updateBtn.setOnAction(e-> openWarehouseUpdate(warehouse));

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e-> deleteWarehouse(warehouse));

        HBox hbox = new HBox(nameLabel, locationLabel,updateBtn,deleteBtn);
        hbox.getStyleClass().add("warehouse-box");
        hbox.setSpacing(10);

        return hbox;
    }

    public void deleteWarehouse(Warehouse warehouse){
        String sql = "DELETE FROM warehouse_locations WHERE warehouse_id = ?;";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1,warehouse.getId());
            stmt.executeUpdate();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete!!");
            alert.setHeaderText("Warehouse Deleted!!");
            alert.setContentText("Warehouse Deleted Succsesfuly");
            alert.showAndWait();
            showWarehouses();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openWarehouseUpdate(Warehouse warehouse){
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/updateWarehouse.fxml"));
            Parent root = loader.load();

            UpdateWarehouseController controller = loader.getController();
            controller.setMainController(this);
            controller.setWarehouse(warehouse);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Update Warehouse");
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }


    }





    public void searchWarehouse() {
        String searchValue = searchWarehouse.getText();

        List<Warehouse> warehouses = getWarehouses(
                "SELECT * FROM warehouse_locations WHERE " +
                        "location_name ILIKE '%" + searchValue + "%' OR " +
                        "address ILIKE '%" + searchValue + "%';"
        );

        warehouseList.getChildren().clear();
        for (Warehouse warehouse : warehouses) {
            warehouseList.getChildren().add(createWarehouseBox(warehouse));
        }
    }

    private List<Warehouse> getWarehouses(String sql) {
        List<Warehouse> warehouses = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("warehouse_id");
                String name = rs.getString("location_name");
                String location = rs.getString("address");

                warehouses.add(new Warehouse(id, name, location));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return warehouses;
    }
}
