package com.app.invordersys.controllers;

import com.app.invordersys.models.Category;
import com.app.invordersys.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UpdateProductController {


    @FXML
    public TextField nameField;
    @FXML
    public TextField priceField;

    @FXML
    public TextField stockField;
    @FXML
    public ComboBox categoryField;
    @FXML
    public Button updateBtn;


    private ProductController mainController;
    private Product product;

    public void setMainController(ProductController controller) {this.mainController = controller;}



    @FXML
    public void initialize(){
        updateBtn.setOnAction(e-> updateProduct());
    }


    private List<Category> loadCategory(){

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





        }catch (Exception e){
            e.printStackTrace();
        }
        return categories;


    }


    public void setProduct(Product product){
        this.product=product;

        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
        List<Category> categories = loadCategory();
        categoryField.getItems().addAll(categories);

    }


    public void updateProduct(){
        String name = nameField.getText();
        BigDecimal price=new BigDecimal(priceField.getText());
        int stock=Integer.parseInt(stockField.getText());
        Category selectedCategory = (Category) categoryField.getValue();

        String sql = "UPDATE products SET name = ?, price = ?, stock_quantity = ?, category_id = ? WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setBigDecimal(2, price);
            stmt.setInt(3, stock);
            stmt.setInt(4,selectedCategory.getId());
            stmt.setInt(5, product.getProductId());

            stmt.executeUpdate();

            mainController.showProducts();


            Stage stage = (Stage) updateBtn.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }




}
