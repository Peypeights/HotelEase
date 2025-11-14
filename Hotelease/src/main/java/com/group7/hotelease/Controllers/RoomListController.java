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
import javafx.scene.layout.VBox;

public class RoomListController {
    @FXML private VBox roomListContainer;
    
    public static String selectedHotelId = "";
    public static String selectedHotelName = "";

    @FXML
    public void initialize() {
        // TODO: Load rooms from CSV based on selected hotel
        // Create room cards dynamically and add to roomListContainer
    }

    @FXML
    public void goBack() {
        SceneManager.goBack();
    }
}