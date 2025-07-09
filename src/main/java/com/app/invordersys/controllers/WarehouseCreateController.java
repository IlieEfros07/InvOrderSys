package com.app.invordersys.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class WarehouseCreateController {

    @FXML
    public TextField locationNameField;

    @FXML
    public TextField addressField;

    private WarehouseController mainController;
    public void setMainController(WarehouseController controller)
    {
        this.mainController=controller;
    }


    @FXML
    public void initialize(){

    }

    @FXML
    public void handleSubmit(){
        String location=locationNameField.getText();
        String address=locationNameField.getText();
        String sql = "INSERT INTO warehouse_locations(location_name,address) VALUES(?,?)";

        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String pswd="root";


        try(Connection conn = DriverManager.getConnection(url,user,pswd);
            PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setString(1,location);
            stmt.setString(2,address);
            stmt.executeUpdate();

            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Succes!");
            alert.setContentText("Warehouse Location Added");
            alert.showAndWait();
            locationNameField.clear();
            addressField.clear();



        }catch(Exception e){
            e.printStackTrace();
        }




    }

















}
