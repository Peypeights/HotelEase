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
        SceneManager.switchScene("adminBookings.fxml", "Manage Bookings");
    }

    @FXML
    public void manageRooms() {
        SceneManager.switchScene("adminHotelSelector.fxml", "Manage Rooms");
    }

    @FXML
        public void manageHotels() {
        SceneManager.switchScene("manageHotels.fxml", "Manage Hotels");
    }

    @FXML
    public void logout() {
        SceneManager.clearHistory();
        LoginController.currentUser = null;
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}
