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
import com.group7.hotelease.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.util.List;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    // Store currently logged in user (simple session management)
    public static User currentUser = null;
    
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Checks if it's empty
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }
        
        // Hardcoded admin account for testing
        if (username.equals("admin") && password.equals("admin123")) {
            User newUser = new User("0", "admin", "admin123", "admin");
            
            if (currentUser == null) {
                currentUser = newUser;
            } else {
                // allow if same userId, block if different
                if (!safeEquals(currentUser.getUserId(), newUser.getUserId())) {
                    errorLabel.setText("Another account is currently logged in");
                    return;
                } else {
                    // same user logging in again — refresh session
                    currentUser = newUser;
                }
            }
            
            SceneManager.clearHistory();
            SceneManager.switchScene("adminDashboard.fxml", "Admin Dashboard");
            return;
        }
        
        // Read users from CSV
        List<String[]> users = CSVManager.readCSV("users.csv");
        
        // Skip header row then search for user
        boolean found = false;
        for (int i = 1; i < users.size(); i++) {
            String[] userData = users.get(i);
            
            // CSV format: userId,username,password,accountType
            if (userData.length >= 4) {
                String csvUserId = userData[0].trim();
                String csvUsername = userData[1].trim();
                String csvPassword = userData[2].trim();
                String accountType = userData[3].trim();
                
                // Debugging (optional)
                System.out.println("Comparing: '" + username + "' with '" + csvUsername + "'");
                System.out.println("Password: '" + password + "' with '" + csvPassword + "'");
                
                if (csvUsername.equals(username) && csvPassword.equals(password)) {
                    found = true;
                    
                    // Create user object from CSV row
                    User newUser = new User(csvUserId, csvUsername, csvPassword, accountType);
                    
                    if (currentUser == null) {
                        // nobody logged in — accept
                        currentUser = newUser;
                    } else {
                        // someone is logged in — only block if it's a different user
                        String activeId = currentUser.getUserId();
                        String incomingId = newUser.getUserId();
                        if (activeId == null || !activeId.equals(incomingId)) {
                            errorLabel.setText("Another account is currently logged in");
                            return;
                        } else {
                            // Same user logging in again: refresh session
                            currentUser = newUser;
                        }
                    }
                    
                    // Navigate based on account type
                    SceneManager.clearHistory();
                    if (accountType.equalsIgnoreCase("admin")) {
                        SceneManager.switchScene("adminDashboard.fxml", "Admin Dashboard");
                    } else {
                        SceneManager.switchScene("hotelSelection.fxml", "Select a Hotel");
                    }
                    return;
                }
            }
        }
        
        // If not found
        if (!found) {
            errorLabel.setText("Invalid username or password");
        }
    }
    
    @FXML
    public void handleKeyPress(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            handleLogin();
        }
    }
    
    @FXML
    public void goToRegistration() {
        SceneManager.switchScene("registration.fxml", "Register Account");
    }

    // small helper to avoid NPEs when comparing IDs
    private boolean safeEquals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}