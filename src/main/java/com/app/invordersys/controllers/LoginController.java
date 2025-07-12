package com.app.invordersys.controllers;

import com.app.invordersys.MainApplication;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class LoginController {

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    @FXML
    public Button loginButton;
    @FXML
    public Label errorMessageLabel;


    public void initialize(){
        loginButton.setOnAction(e -> handleLogin());
    }


    public void handleLogin(){
        String username=usernameField.getText();
        String password= passwordField.getText();
        if(authUser(username,password)){
            try{
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/main-layout.fxml"));
                Parent root = loader.load();


                Scene scene = new Scene(root);
                scene.getStylesheets().add(
                        Objects.requireNonNull(MainApplication.class.getResource("/css/style.css")).toExternalForm()
                );

                Stage stage = new Stage();
                stage.setTitle("Inventory & Order System");
                stage.setScene(scene);
                stage.show();


                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.close();




            } catch (Exception e) {
                e.printStackTrace();

            }
        }else{
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText("Failed!");
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }





    }

    private boolean authUser(String username, String password){
        String url = "jdbc:postgresql://localhost:5432/invordersys";
        String user = "root";
        String pswd = "root";

        String sql = "SELECT password FROM users WHERE username = ?";
        try(Connection conn = DriverManager.getConnection(url,user,pswd);
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                String passwordDB = rs.getString("password");
                return BCrypt.checkpw(password,passwordDB);
            }

        } catch (Exception e) {
          e.printStackTrace();

        }

        return false;

    }









}
