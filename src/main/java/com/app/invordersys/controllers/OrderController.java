package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderController {


    @FXML
    public Button orderCreate;


    @FXML
    public void initialize() {
        orderCreate.setOnAction(e -> openOrderCreate());

    }

    private void openOrderCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/orderCreate.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Add Order");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
