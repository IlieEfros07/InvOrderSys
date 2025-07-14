package com.app.invordersys.controllers;

import com.app.invordersys.models.Supplier;
import com.app.invordersys.models.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateSupplierController {
    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public Button updateBtn;

    private SupplierController mainController;
    private Supplier supplier;
    public void setMainController(SupplierController controller) {this.mainController = controller;}

    @FXML
    public void initialize(){
        updateBtn.setOnAction(e-> updateSupplier());
    }




    public void setSupplier(Supplier supplier){
        this.supplier=supplier;
        nameField.setText(supplier.getName());
        emailField.setText(supplier.getEmail());
        phoneField.setText(supplier.getPhone());



    }


    public void updateSupplier(){
        String name = nameField.getText();
        String email=emailField.getText();
        String phone=phoneField.getText();

        String sql = "UPDATE suppliers SET name = ?, contact_email = ?, phone = ? WHERE supplier_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setInt(4,supplier.getId());

            stmt.executeUpdate();

            mainController.showSuppliers();


            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}
