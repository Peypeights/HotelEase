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

public class HotelSelectionController {

    @FXML
    public void selectCoastalBliss() {
        HotelListController.selectedHotelType = "coastal";
        SceneManager.switchScene("hotelList.fxml", "Coastal Bliss Hotels");
    }

    @FXML
    public void selectHighlandHaven() {
        HotelListController.selectedHotelType = "highland";
        SceneManager.switchScene("hotelList.fxml", "Highland Haven Hotels");
    }

    @FXML
    public void selectUrbanEscape() {
        HotelListController.selectedHotelType = "urban";
        SceneManager.switchScene("hotelList.fxml", "Urban Escape Hotels");
    }

    @FXML
    public void logout() {
        LoginController.currentUser = null;
        SceneManager.clearHistory();
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}