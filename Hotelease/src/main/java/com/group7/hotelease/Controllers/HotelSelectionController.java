/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group7.hotelease.Controllers;

import com.group7.hotelease.Utils.SceneManager;
import javafx.fxml.FXML;
/**
 *
 * @author lapid
 */
public class HotelSelectionController {

    @FXML
    public void selectCoastalBliss() {
        // TODO: Pass hotel ID to room list
        SceneManager.switchScene("roomList.fxml", "Coastal Bliss - Rooms");
    }

    @FXML
    public void selectHighlandHaven() {
        SceneManager.switchScene("roomList.fxml", "Highland Haven - Rooms");
    }

    @FXML
    public void selectUrbanEscape() {
        SceneManager.switchScene("roomList.fxml", "Urban Escape - Rooms");
    }

    @FXML
    public void logout() {
        SceneManager.clearHistory();
        LoginController.currentUser = null;
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}