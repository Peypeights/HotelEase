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
import com.group7.hotelease.Utils.CSVManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import java.util.List;

public class RegistrationController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> accountTypeCombo;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        // checheck nya kung may admin na naka login
        if (LoginController.currentUser != null && 
            LoginController.currentUser.getAccountType().equalsIgnoreCase("admin")) {
            // Kung admin nakalogin yung admin, edi pwede mag-choose ung user kung admin or guest type  yung account nila
            accountTypeCombo.getItems().addAll("Guest", "Admin");
        } else {
            // pag hinde naka login yung admin account, edi automatic guest account lang ang pwedeng piliin sa registration
            accountTypeCombo.getItems().add("Guest");
        }
        accountTypeCombo.setValue("Guest");
    }
    
    // para sa ano lang lang toh, kapag nag-press enter, automatic mag hahandle registration
    @FXML
    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRegister();
        }
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String accountType = accountTypeCombo.getValue().toLowerCase();
        
        // Clear previous error duh
        errorLabel.setText("");
        
        // check kung empty yung mga fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }
        
        // need minimum of 4 characters para sa username
        if (username.length() < 4) {
            errorLabel.setText("Username must be at least 4 characters");
            return;
        }
        
        // bawal rin spaces sa username
        if (username.contains(" ")) {
            errorLabel.setText("Username cannot contain spaces");
            return;
        }
        
        // need naman minimum 8 characters para sa password
        if (password.length() < 8) {
            errorLabel.setText("Password must be at least 8 characters");
            return;
        }
        
        // checheck nya lang kung yung password ay parehas sa confirmed password
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }
        
        // checheck nya if existing username na sa database. Basically bawal duplicates na username pero pwede sa password
        List<String[]> users = CSVManager.readCSV("users.csv");
        for (int i = 1; i < users.size(); i++) {
            String[] userData = users.get(i);
            if (userData.length >= 2 && userData[1].trim().equals(username)) {
                errorLabel.setText("Username already exists");
                return;
            }
        }
        
        // Generate new user ID
        int newUserId = 1;
        if (users.size() > 1) {
            // Get the last user's ID and increment
            String lastId = users.get(users.size() - 1)[0];
            try {
                newUserId = Integer.parseInt(lastId) + 1;
            } catch (NumberFormatException e) {
                // kung nag-fail, edi use size as ID
                newUserId = users.size();
            }
        }
        
        // Create new user object/instance
        String[] newUser = {
            String.valueOf(newUserId),
            username,
            password,
            accountType
        };
        
        // lalagay sa csv
        CSVManager.appendCSV("users.csv", newUser);
        
        // Mag aalert na successful yung pag-create
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText("Account created successfully! You can now login.");
        alert.showAndWait();
        
        // Go back to login page
        SceneManager.goBack();
    }

    @FXML
    public void backToLogin() {
        SceneManager.goBack();
    }
}