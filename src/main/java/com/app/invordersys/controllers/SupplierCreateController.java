package com.app.invordersys.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class SupplierCreateController {

    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;

    @FXML
    public void initialize(){

    }

    @FXML
    public void handleSubmit(){

        String name = nameField.getText();
        String email=emailField.getText();
        String phone=phoneField.getText();

        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String pswd="root";

        String sql="INSERT INTO suppliers(name,contact_email,phone) VALUES(?,?,?)";


        try(Connection conn = DriverManager.getConnection(url,user,pswd);
            PreparedStatement stmt=conn.prepareStatement(sql);){

            stmt.setString(1,name);
            stmt.setString(2,email);
            stmt.setString(3,phone);
            stmt.executeUpdate();


            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Succes!");
            alert.setContentText("Supplier Added");
            alert.showAndWait();


        }catch (Exception e){
            e.printStackTrace();
        }



    }




}
