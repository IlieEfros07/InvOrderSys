package com.app.invordersys.controllers;

import com.app.invordersys.models.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.sql.*;


public class PoductCreateController {
    @FXML
    public TextField nameField;
    @FXML
    public TextField priceField;

    @FXML
    public TextField stockField;
    @FXML
    public ComboBox categoryField;

    private ProductController mainController;
    public void setMainController(ProductController controller)
    {
        this.mainController=controller;
    }

    @FXML
    public void initialize(){
        loadCategory();
    }


    @FXML
    public void handleSubmit(){
        String name = nameField.getText();
        String price = priceField.getText();
        String stock = stockField.getText();
        Category selectedCategory = (Category) categoryField.getValue();
        System.out.println(selectedCategory.getId());



        String sql = "INSERT INTO products(name,price,stock_quantity,category_id) VALUES(?,?,?,?)";
        try(Connection conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys","root","root"); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,name);
            stmt.setBigDecimal(2, new BigDecimal(price));
            stmt.setInt(3, Integer.parseInt(stock));
            stmt.setInt(4,selectedCategory.getId());
            stmt.executeUpdate();

            Alert alert= new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Success!");
            alert.setContentText("Product Added!");
            alert.showAndWait();
            nameField.clear();;
            priceField.clear();
            stockField.clear();


            if(mainController!= null){
                mainController.showProducts();
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }


    private void loadCategory(){

        ObservableList<Category> categories = FXCollections.observableArrayList();

        String sql="SELECT * FROM categories";
        try(Connection conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys","root","root");
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while(rs.next()){

                int id = rs.getInt("category_id");
                String name = rs.getString("category_name");

                categories.add(new Category(id,name));
            }

            categoryField.setItems(categories);



        }catch (Exception e){
            e.printStackTrace();
        }


    }







}
