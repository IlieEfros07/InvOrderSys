package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.app.invordersys.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductController {

    @FXML
    public Button productCreate;

    @FXML
    public VBox productList;

    @FXML
    public TextField searchProduct;
    @FXML
    public Button searchProductBtn;


    @FXML
    public void initialize(){
        productCreate.setOnAction(e->openProductCrete());
        searchProductBtn.setOnAction(e->searchProduct());

        showProducts();
    }

   private void  openProductCrete(){
        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/productCreate.fxml"));
            Parent root = loader.load();

            PoductCreateController controller = loader.getController();
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


   public void showProducts(){

        List<Product>products=getProduct("SELECT * FROM products;");

        productList.getChildren().clear();

        for(Product product:products){
            productList.getChildren().add(createProductBox(product));
        }

   }

   private HBox createProductBox(Product product){

       Label nameLabel=new Label(product.getName());
       Label categoryLabel = new Label(String.valueOf(product.getCategoryId()));
       Label priceLabel = new Label(String.format("%.2f", product.getPrice()));
       Label stockLabel = new Label(String.valueOf(product.getStock()));

       HBox hbox=new HBox(nameLabel,categoryLabel,priceLabel,stockLabel);
       hbox.getStyleClass().add("products-box");
       hbox.setSpacing(10);

       return hbox;


   }

   public void searchProduct(){

        String searchValue=searchProduct.getText();
       List<Product> products = getProduct("SELECT * FROM products WHERE name ILIKE '%" +
               searchValue + "%' OR CAST(price AS TEXT) ILIKE '%" + searchValue + "%';");

       productList.getChildren().clear();

       for(Product product:products){
           productList.getChildren().add(createProductBox(product));
       }


   }



   private List<Product> getProduct(String sql){
        List<Product> products = new ArrayList<>();

        String url="jdbc:postgresql://localhost:5432/invordersys";
        String user="root";
        String password="root";

//        String sql="SELECT * FROM products";



        try(Connection conn = DriverManager.getConnection(url,user,password);
            Statement stmt=conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()){
                int id = rs.getInt("product_id");
                String name=rs.getString("name");
                int categoryId=rs.getInt("category_id");
                BigDecimal price = rs.getBigDecimal("price");
                int stock=rs.getInt("stock_quantity");



                products.add(new Product(id,name,price,stock,categoryId));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;



   }




}
