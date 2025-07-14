package com.app.invordersys.controllers;

import com.app.invordersys.models.Category;
import com.app.invordersys.models.Product;
import com.app.invordersys.models.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class UpdateWarehouseController {
    @FXML
    public TextField locationNameField;

    @FXML
    public TextField addressField;
    @FXML
    public Button updateBtn;

    private WarehouseController mainController;
    private Warehouse warehouse;

    @FXML
    public void initialize(){
        updateBtn.setOnAction(e-> updateWarehouse());
    }


    public void setMainController(WarehouseController controller) {
        this.mainController = controller;
    }

    public void setWarehouse(Warehouse warehouse){
        this.warehouse=warehouse;
        locationNameField.setText(warehouse.getName());
        addressField.setText(warehouse.getLocation());


    }


    public void updateWarehouse(){
        String location=locationNameField.getText();
        String address=locationNameField.getText();

        String sql = "UPDATE warehouse_locations SET location_name = ?, address = ? WHERE warehouse_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, location);
            stmt.setString(2, address);
            stmt.setInt(3, warehouse.getId());

            stmt.executeUpdate();

            mainController.showWarehouses();


            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }



}
