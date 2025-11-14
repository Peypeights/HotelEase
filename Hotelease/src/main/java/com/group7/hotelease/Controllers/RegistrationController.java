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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class RegistrationController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> accountTypeCombo;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        accountTypeCombo.getItems().addAll("Guest", "Admin");
        accountTypeCombo.setValue("Guest");
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String accountType = accountTypeCombo.getValue().toLowerCase();
        
        // TODO: Add registration logic here
        // Check if passwords match
        // Save to users.csv
        // Navigate to login
        
        errorLabel.setText("Registration functionality to be implemented");
    }

    @FXML
    public void backToLogin() {
        SceneManager.goBack();
    }
}