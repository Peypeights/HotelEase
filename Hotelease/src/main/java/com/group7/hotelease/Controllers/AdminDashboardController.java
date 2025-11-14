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
import com.group7.hotelease.Controllers.LoginController;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    public void manageBookings() {
        // TODO: Open bookings management scene
    }

    @FXML
    public void manageRooms() {
        // TODO: Open rooms management scene
    }

    @FXML
    public void manageHotels() {
        // TODO: Open hotels management scene
    }

    @FXML
    public void logout() {
        SceneManager.clearHistory();
        LoginController.currentUser = null;
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}
