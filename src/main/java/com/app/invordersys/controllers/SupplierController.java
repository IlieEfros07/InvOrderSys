package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SupplierController {

    @FXML
    public Button supplierCreate;


    @FXML
    public void initialize(){
        supplierCreate.setOnAction(e->openSupplierCreate());

    }

    private void  openSupplierCreate(){
        try{
            FXMLLoader loader=new FXMLLoader(MainApplication.class.getResource("fxml/supplierCreate.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Add Supplier");
            stage.setScene(new Scene(root));
            stage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
