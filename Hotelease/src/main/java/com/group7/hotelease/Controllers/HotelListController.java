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
import com.group7.hotelease.Models.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;
import java.util.ArrayList;

public class HotelListController {
    @FXML private Label titleLabel;
    @FXML private GridPane hotelGrid;
    
    // This will be set by HotelSelectionController to filter hotels
    public static String selectedHotelType = "";
    
    @FXML
    public void initialize() {
        loadHotels();
        
        // Update title based on selected type
        String displayTitle = "";
        switch (selectedHotelType.toLowerCase()) {
            case "coastal":
                displayTitle = "Coastal Bliss Hotels";
                break;
            case "highland":
                displayTitle = "Highland Haven Hotels";
                break;
            case "urban":
                displayTitle = "Urban Escape Hotels";
                break;
            default:
                displayTitle = "Available Hotels";
        }
        titleLabel.setText(displayTitle);
    }
    
    private void loadHotels() {
        // Read hotels from CSV
        List<String[]> hotelsData = CSVManager.readCSV("hotels.csv");
        List<Hotel> hotels = new ArrayList<>();
        
        // Parse CSV data (skip header)
        for (int i = 1; i < hotelsData.size(); i++) {
            String[] data = hotelsData.get(i);
            if (data.length >= 5) {
                Hotel hotel = new Hotel(
                    data[0].trim(), // hotelId
                    data[1].trim(), // hotelName
                    data[2].trim(), // hotelType
                    data[3].trim(), // location
                    data[4].trim()  // description
                );
                
                // Filter by selected type
                if (hotel.getHotelType().equalsIgnoreCase(selectedHotelType)) {
                    hotels.add(hotel);
                }
            }
        }
        
        // Display hotels in grid
        displayHotelsGrid(hotels);
    }
    
    private void displayHotelsGrid(List<Hotel> hotels) {
        hotelGrid.getChildren().clear();
        
        if (hotels.isEmpty()) {
            // Show "No hotels available" message
            Label noHotelsLabel = new Label("No hotels available in this category");
            noHotelsLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 16px;");
            hotelGrid.add(noHotelsLabel, 0, 0);
            return;
        }
        
        int column = 0;
        int row = 0;
        
        for (Hotel hotel : hotels) {
            VBox hotelCard = createHotelCard(hotel);
            hotelGrid.add(hotelCard, column, row);
            
            column++;
            if (column == 2) { // 2 hotels per row
                column = 0;
                row++;
            }
        }
    }
    
    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle(
            "-fx-background-color: #2a2a2a; " +
            "-fx-background-radius: 15; " +
            "-fx-padding: 30; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );
        card.setPrefWidth(350);
        card.setPrefHeight(280);
        
        // Hotel icon based on type
        String icon = "🏨";
        if (hotel.getHotelType().equalsIgnoreCase("coastal")) {
            icon = "🏖️";
        } else if (hotel.getHotelType().equalsIgnoreCase("highland")) {
            icon = "⛰️";
        } else if (hotel.getHotelType().equalsIgnoreCase("urban")) {
            icon = "🌆";
        }
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 48px;");
        
        Label nameLabel = new Label(hotel.getHotelName());
        nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(290);
        
        Label locationLabel = new Label("📍 " + hotel.getLocation());
        locationLabel.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 13px;");
        locationLabel.setWrapText(true);
        locationLabel.setMaxWidth(290);
        
        Label descLabel = new Label(hotel.getDescription());
        descLabel.setStyle("-fx-text-fill: #909090; -fx-font-size: 12px;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(290);
        descLabel.setMaxHeight(60);
        
        Button viewRoomsBtn = new Button("View Rooms →");
        viewRoomsBtn.setStyle(
            "-fx-background-color: #4a9eff; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 10 20;"
        );
        viewRoomsBtn.setPrefWidth(200);
        
        // When clicked, go to room list for this hotel
        viewRoomsBtn.setOnAction(e -> {
            RoomListController.selectedHotelId = hotel.getHotelId();
            RoomListController.selectedHotelName = hotel.getHotelName();
            SceneManager.switchScene("roomList.fxml", hotel.getHotelName() + " - Rooms");
        });
        
        // Hover effect
        viewRoomsBtn.setOnMouseEntered(e -> 
            viewRoomsBtn.setStyle(
                "-fx-background-color: #3a8eef; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 10 20;"
            )
        );
        viewRoomsBtn.setOnMouseExited(e -> 
            viewRoomsBtn.setStyle(
                "-fx-background-color: #4a9eff; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 13px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 10 20;"
            )
        );
        
        card.getChildren().addAll(iconLabel, nameLabel, locationLabel, descLabel, viewRoomsBtn);
        
        return card;
    }
    
    @FXML
    public void goBack() {
        SceneManager.goBack();
    }
    
    @FXML
    public void logout() {
        LoginController.currentUser = null;
        SceneManager.clearHistory();
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}