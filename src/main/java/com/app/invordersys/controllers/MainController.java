package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    public AnchorPane contentPane;


    private void loadPage(String fxml){
        try{
            Parent root= FXMLLoader.load(MainApplication.class.getResource("fxml/" +fxml));
            contentPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showProducts(){
        loadPage("products.fxml");
    }
    @FXML
    public void showOrders(){
        loadPage("orders.fxml");
    }
    @FXML
    public void showWarehouses(){
        loadPage("warehouses.fxml");
    }
    @FXML
    public void showCustomers(){
        loadPage("customers.fxml");
    }
    @FXML
    public void showSupplier(){
        loadPage("suppliers.fxml");
    }
    @FXML
    public void showRestockRequests(){
        loadPage("restockRequests.fxml");
    }



}
