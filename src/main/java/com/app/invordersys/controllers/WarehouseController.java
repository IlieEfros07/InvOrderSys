package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WarehouseController {

    @FXML
    public Button warehouseCreate;


    @FXML
    public void initialize(){
        warehouseCreate.setOnAction(e->openWarehouseCrete());

    }

    private void  openWarehouseCrete(){
        try{
            FXMLLoader loader=new FXMLLoader(MainApplication.class.getResource("fxml/warehouseCreate.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Add Warehouse");
            stage.setScene(new Scene(root));
            stage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
