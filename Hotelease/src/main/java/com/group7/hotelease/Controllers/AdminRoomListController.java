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
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminRoomListController {

    // Set by AdminHotelSelectorController when admin opens manage rooms
    public static String selectedHotelId = null;
    public static String selectedHotelName = null;

    @FXML private GridPane roomGrid;
    @FXML private Label titleLabel;

    @FXML
    public void initialize() {
        if (selectedHotelName != null) titleLabel.setText(selectedHotelName + " — Manage Rooms");
        loadRooms();
    }

    private void loadRooms() {
        List<String[]> rows = CSVManager.readCSV("rooms.csv");
        List<String[]> roomsForHotel = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length >= 6) {
                String hotelId = r[1].trim();
                if (hotelId.equals(selectedHotelId)) roomsForHotel.add(r);
            }
        }

        displayGrid(roomsForHotel);
    }

    private void displayGrid(List<String[]> rooms) {
        roomGrid.getChildren().clear();

        if (rooms.isEmpty()) {
            Label empty = new Label("No rooms for this hotel.");
            empty.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 14px;");
            roomGrid.add(empty, 0, 0);
            return;
        }

        int col = 0, row = 0;
        for (String[] r : rooms) {
            VBox card = createRoomCard(r);
            roomGrid.add(card, col, row);
            col++;
            if (col == 2) { col = 0; row++; }
        }
    }

    private VBox createRoomCard(String[] r) {
        // CSV layout: roomId,hotelId,roomNumber,roomType,price,status
        String roomId = r[0];
        String roomNumber = r[2];
        String roomTypeCode = r[3];
        String priceStr = r[4];
        String status = r[5];

        String roomTypeDisplay = mapCodeToDisplay(roomTypeCode);

        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle(
            "-fx-background-color: #2a2a2a; -fx-background-radius: 12; -fx-padding: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 3);"
        );
        card.setPrefWidth(360);

        Label title = new Label("Room " + roomNumber + " — " + roomTypeDisplay);
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label price = new Label(String.format("%sphp / night", formatPrice(priceStr)));
        price.setStyle("-fx-text-fill: #bdbdbd; -fx-font-size: 13px;");

        Label stat = new Label(statusDisplay(status));
        stat.setStyle("-fx-text-fill: " + statusColor(status) + " -fx-font-weight: bold; -fx-font-size: 12px;");

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_LEFT);

        // Admin actions: Edit + Delete only
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #4a9eff; -fx-border-radius: 8; -fx-text-fill: #4a9eff; -fx-padding: 6 12; -fx-cursor: hand;");
        editBtn.setOnAction(e -> {
            EditRoomController.selectedRoomId = roomId;
            EditRoomController.parentHotelId = selectedHotelId;
            SceneManager.switchScene("editRoom.fxml", "Edit Room " + roomNumber);
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 6 12; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> handleDelete(roomId, roomNumber));

        actions.getChildren().addAll(editBtn, deleteBtn);

        card.getChildren().addAll(title, price, stat, actions);
        return card;
    }

    private String statusDisplay(String status) {
        if (status == null) return "Unknown";
        switch (status.toLowerCase()) {
            case "available": return "Available";
            case "pending":   return "Pending";
            case "booked":    return "Booked";
            default: return status;
        }
    }

    private String statusColor(String status) {
        if (status == null) return "#bdbdbd;";
        switch (status.toLowerCase()) {
            case "available": return "#6be56b;";   // green
            case "pending":   return "#ffd966;";   // yellow (shows pending visually)
            case "booked":    return "#ff6b6b;";   // red
            default: return "#bdbdbd;";
        }
    }

    private String mapCodeToDisplay(String code) {
        if (code == null) return "Single";
        switch (code.toLowerCase()) {
            case "double": return "Double";
            case "deluxe": return "Deluxe Suite";
            default: return "Single";
        }
    }

    private String formatPrice(String priceStr) {
        try {
            double p = Double.parseDouble(priceStr);
            return String.format("%.2f", p);
        } catch (Exception ex) {
            return priceStr;
        }
    }

    private void handleDelete(String roomId, String roomNumber) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Delete room");
        a.setHeaderText(null);
        a.setContentText("Delete room " + roomNumber + " permanently?");
        Optional<ButtonType> res = a.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            List<String[]> data = CSVManager.readCSV("rooms.csv");
            List<String[]> newData = new ArrayList<>();
            if (!data.isEmpty()) newData.add(data.get(0)); // header
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                if (!row[0].trim().equals(roomId)) newData.add(row);
            }
            CSVManager.writeCSV("rooms.csv", newData);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Deleted");
            info.setHeaderText(null);
            info.setContentText("Room deleted.");
            info.showAndWait();

            loadRooms();
        }
    }

    @FXML public void onAddRoom() {
        AddRoomController.parentHotelId = selectedHotelId;
        AddRoomController.parentHotelName = selectedHotelName;
        SceneManager.switchScene("addRoom.fxml", "Add Room");
    }

    @FXML public void goBack() { SceneManager.goBack(); }

    @FXML public void logout() {
        LoginController.currentUser = null;
        SceneManager.clearHistory();
        SceneManager.switchScene("login.fxml", "Hotel Booking System");
    }
}