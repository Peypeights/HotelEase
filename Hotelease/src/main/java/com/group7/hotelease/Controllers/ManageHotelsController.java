/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group7.hotelease.Controllers;

/**
 *
 * @author lapid
 */
import com.group7.hotelease.Utils.CSVManager;
import com.group7.hotelease.Utils.SceneManager;
import com.group7.hotelease.Models.Hotel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageHotelsController {
    @FXML private GridPane hotelGrid;

    @FXML
    public void initialize() {
        loadHotels();
    }

    private void loadHotels() {
        List<String[]> hotelsData = CSVManager.readCSV("hotels.csv");
        List<Hotel> hotels = new ArrayList<>();

        for (int i = 1; i < hotelsData.size(); i++) {
            String[] d = hotelsData.get(i);
            if (d.length >= 5) {
                hotels.add(new Hotel(d[0].trim(), d[1].trim(), d[2].trim(), d[3].trim(), d[4].trim()));
            }
        }

        displayHotelsGrid(hotels);
    }

    private void displayHotelsGrid(List<Hotel> hotels) {
        hotelGrid.getChildren().clear();

        if (hotels.isEmpty()) {
            Label noHotels = new Label("No hotels found");
            noHotels.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 16px;");
            hotelGrid.add(noHotels, 0, 0);
            return;
        }

        int col = 0, row = 0;
        for (Hotel h : hotels) {
            VBox card = createHotelCard(h);
            hotelGrid.add(card, col, row);
            col++;
            if (col == 2) { col = 0; row++; }
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle(
            "-fx-background-color: #2a2a2a; " +
            "-fx-background-radius: 12; " +
            "-fx-padding: 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 3);"
        );
        card.setPrefWidth(340);

        Label name = new Label(hotel.getHotelName());
        name.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label type = new Label(hotel.getHotelType());
        type.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");

        Label location = new Label("📍 " + hotel.getLocation());
        location.setStyle("-fx-text-fill: #909090; -fx-font-size: 12px;");

        Label desc = new Label(hotel.getDescription());
        desc.setStyle("-fx-text-fill: #909090; -fx-font-size: 12px;");
        desc.setWrapText(true);
        desc.setMaxWidth(300);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #4a9eff; -fx-border-radius: 8; -fx-text-fill: #4a9eff; -fx-padding: 8 16; -fx-cursor: hand;");
        editBtn.setOnAction(e -> {
            EditHotelController.selectedHotelId = hotel.getHotelId();
            SceneManager.switchScene("editHotel.fxml", "Edit Hotel - " + hotel.getHotelName());
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 16; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> handleDelete(hotel.getHotelId(), hotel.getHotelName()));

        Button manageRoomsBtn = new Button("Rooms");
        manageRoomsBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 12; -fx-cursor: hand;");
        manageRoomsBtn.setOnAction(e -> {
            RoomListController.selectedHotelId = hotel.getHotelId();
            RoomListController.selectedHotelName = hotel.getHotelName();
            SceneManager.switchScene("roomList.fxml", hotel.getHotelName() + " - Rooms");
        });

        actions.getChildren().addAll(editBtn, deleteBtn, manageRoomsBtn);

        card.getChildren().addAll(name, type, location, desc, actions);
        return card;
    }

    private void handleDelete(String hotelId, String hotelName) {
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Delete Hotel");
        conf.setHeaderText(null);
        conf.setContentText("Delete '" + hotelName + "' permanently?");
        Optional<ButtonType> res = conf.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            List<String[]> data = CSVManager.readCSV("hotels.csv");
            // keep header, remove matching id row
            List<String[]> newData = new ArrayList<>();
            if (!data.isEmpty()) newData.add(data.get(0));
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                if (row.length > 0 && !row[0].trim().equals(hotelId)) {
                    newData.add(row);
                }
            }
            CSVManager.writeCSV("hotels.csv", newData);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Deleted");
            info.setHeaderText(null);
            info.setContentText("Hotel deleted successfully.");
            info.showAndWait();

            loadHotels();
        }
    }

    @FXML
    public void onAddHotel() {
        SceneManager.switchScene("addHotel.fxml", "Add New Hotel");
    }

    @FXML
    public void goBack() { SceneManager.goBack(); }

    @FXML
    public void logout() {
        LoginController.currentUser = null;
        SceneManager.clearHistory();
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}