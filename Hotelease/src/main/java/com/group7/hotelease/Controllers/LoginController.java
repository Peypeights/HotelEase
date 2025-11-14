/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group7.hotelease.Controllers;

/**
 *
 * @author lapid
 */
import com.group7.hotelease.Utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // TODO: Add authentication logic here
        // For now, just check account type and navigate
        
        // Example: If admin
        // SceneManager.switchScene("adminDashboard.fxml", "Admin Dashboard");
        
        // Example: If guest
        // SceneManager.switchScene("hotelSelection.fxml", "Select a Hotel");
        
        errorLabel.setText("Login functionality to be implemented");
    }

    @FXML
    public void goToRegistration() {
        SceneManager.switchScene("registration.fxml", "Register Account");
    }
}