package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.app.invordersys.models.Supplier;
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

public class SupplierController {

    @FXML
    public Button supplierCreate;

    @FXML
    public VBox supplierList;

    @FXML
    public TextField searchSupplier;

    @FXML
    public Button searchSupplierBtn;

    @FXML
    public void initialize() {
        supplierCreate.setOnAction(e -> openSupplierCreate());
        searchSupplierBtn.setOnAction(e -> searchSupplier());

        showSuppliers();
    }

    private void openSupplierCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/supplierCreate.fxml"));
            Parent root = loader.load();



            SupplierCreateController controller = loader.getController();
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

    public void showSuppliers() {
        List<Supplier> suppliers = getSuppliers("SELECT * FROM suppliers;");
        supplierList.getChildren().clear();

        for (Supplier supplier : suppliers) {
            supplierList.getChildren().add(createSupplierBox(supplier));
        }
    }

    private HBox createSupplierBox(Supplier supplier) {
        Label nameLabel = new Label(supplier.getName());
        Label emailLabel = new Label(supplier.getEmail());
        Label phoneLabel = new Label(supplier.getPhone());

        Button updateBtn = new Button("Update");
        updateBtn.getStyleClass().add("update-button");
        updateBtn.setOnAction(e-> openSupplierUpdate(supplier));

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e-> deleteSupplier(supplier));

        HBox hbox = new HBox(nameLabel, emailLabel, phoneLabel,updateBtn,deleteBtn);
        hbox.getStyleClass().add("supplier-box");
        hbox.setSpacing(10);

        return hbox;
    }



    public void deleteSupplier(Supplier supplier){
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?;";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1,supplier.getId());
            stmt.executeUpdate();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete!!");
            alert.setHeaderText("Warehouse Deleted!!");
            alert.setContentText("Warehouse Deleted Succsesfuly");
            alert.showAndWait();
            showSuppliers();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openSupplierUpdate(Supplier supplier){
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/updateSupplier.fxml"));
            Parent root = loader.load();

            UpdateSupplierController controller = loader.getController();
            controller.setMainController(this);
            controller.setSupplier(supplier);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Update supplier");
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }


    }



    public void searchSupplier() {
        String searchValue = searchSupplier.getText();

        List<Supplier> suppliers = getSuppliers(
                "SELECT * FROM suppliers WHERE " +
                        "name ILIKE '%" + searchValue + "%' OR " +
                        "email ILIKE '%" + searchValue + "%' OR " +
                        "phone ILIKE '%" + searchValue + "%';"
        );

        supplierList.getChildren().clear();
        for (Supplier supplier : suppliers) {
            supplierList.getChildren().add(createSupplierBox(supplier));
        }
    }

    private List<Supplier> getSuppliers(String sql) {
        List<Supplier> suppliers = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("supplier_id");
                String name = rs.getString("name");
                String email = rs.getString("contact_email");
                String phone = rs.getString("phone");


                suppliers.add(new Supplier(id, name, email, phone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return suppliers;
    }
}
