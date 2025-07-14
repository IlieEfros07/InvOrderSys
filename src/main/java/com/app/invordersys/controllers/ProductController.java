package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.app.invordersys.models.Product;
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
            stage.setTitle("Add Product");
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

       Button updateBtn = new Button("Update");
       updateBtn.getStyleClass().add("update-button");
       updateBtn.setOnAction(e-> openProductUpdate(product));

       Button deleteBtn = new Button("Delete");
       deleteBtn.getStyleClass().add("delete-button");
       deleteBtn.setOnAction(e-> deleteProduct(product));

       HBox hbox=new HBox(nameLabel,categoryLabel,priceLabel,stockLabel,updateBtn,deleteBtn);
       hbox.getStyleClass().add("products-box");
       hbox.setSpacing(10);

       return hbox;


   }

   public void deleteProduct(Product product){

        String sql = "DELETE FROM products WHERE product_id = ?;";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/invordersys", "root", "root");
            PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1,product.getProductId());
            stmt.executeUpdate();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete!!");
            alert.setHeaderText("Product Deleted!!");
            alert.setContentText("Product Deleted Succsesfuly");
            alert.showAndWait();
            showProducts();


       }catch (Exception e){
            e.printStackTrace();
        }

   }

   public void openProductUpdate(Product product){

        try{
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/updateProduct.fxml"));
            Parent root = loader.load();

            UpdateProductController controller = loader.getController();
            controller.setMainController(this);
            controller.setProduct(product);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Update Product");
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }




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
