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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomListController {
    @FXML private VBox roomListContainer;

    public static String selectedHotelId = "";
    public static String selectedHotelName = "";

    @FXML
    public void initialize() {
        loadRooms(); // populate dynamically
    }

    @FXML
    public void goBack() {
        SceneManager.goBack();
    }

    // Load rows from rooms.csv and create cards for this hotel
    private void loadRooms() {
        roomListContainer.getChildren().clear();

        List<String[]> rows = CSVManager.readCSV("rooms.csv");
        // Expect header: roomId,hotelId,roomNumber,roomType,price,status,userId
        boolean any = false;
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 7) continue; // skip invalid rows
            String hotelId = r[1].trim();
            if (!hotelId.equals(selectedHotelId)) continue;
            any = true;
            HBox card = createRoomCard(r);
            roomListContainer.getChildren().add(card);
        }

        if (!any) {
            Label empty = new Label("No rooms available.");
            empty.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 14px;");
            roomListContainer.getChildren().add(empty);
        }
    }

    private HBox createRoomCard(String[] r) {
        // r: roomId,hotelId,roomNumber,roomType,price,status,userId
        String roomId = r[0];
        String roomNumber = r[2];
        String roomTypeCode = r[3];
        String priceStr = r[4];
        String status = r[5];
        String userId = r[6] != null ? r[6] : ""; // may be empty

        String roomTypeDisplay = mapCodeToDisplay(roomTypeCode);

        HBox card = new HBox(20.0);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        // Left VBox with labels
        VBox left = new VBox(8.0);
        Label lblRoom = new Label("Room " + roomNumber);
        lblRoom.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label lblType = new Label(roomTypeDisplay);
        lblType.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 14px;");
        HBox statRow = new HBox(15.0);
        statRow.setAlignment(Pos.CENTER_LEFT);
        Label statLabel = new Label("Status:");
        statLabel.setStyle("-fx-text-fill: #808080; -fx-font-size: 12px;");
        Label statValue = new Label(statusDisplay(status));
        // correct color application (exact colors requested)
        statValue.setStyle("-fx-text-fill: " + statusColor(status) + "; -fx-font-size: 12px; -fx-font-weight: bold;");
        statRow.getChildren().addAll(statLabel, statValue);
        left.getChildren().addAll(lblRoom, lblType, statRow);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Right VBox with price
        VBox right = new VBox(8.0);
        right.setAlignment(Pos.CENTER_RIGHT);
        Label priceLabel = new Label(String.format("%sphp", formatPrice(priceStr)));
        priceLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 22px; -fx-font-weight: bold;");
        Label perNight = new Label("per night");
        perNight.setStyle("-fx-text-fill: #808080; -fx-font-size: 11px;");
        right.getChildren().addAll(priceLabel, perNight);

        // Action button column (to the far right)
        Button actionBtn = new Button();
        actionBtn.setPrefWidth(120);
        actionBtn.setPrefHeight(40);
        actionBtn.setStyle("-fx-background-color: #4a9eff; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        // Determine label & enabled state
        String currentUserId = getCurrentUserId(); // may be null
        boolean isClickable = false;
        if ("available".equalsIgnoreCase(status)) {
            actionBtn.setText("Book Now");
            isClickable = true;
        } else if ("pending".equalsIgnoreCase(status)) {
            if (userId != null && !userId.isEmpty() && userId.equals(currentUserId)) {
                actionBtn.setText("Pending (Cancel)");
                isClickable = true; // allow cancelling by original requester
            } else {
                actionBtn.setText("Pending");
                isClickable = false;
                actionBtn.setStyle("-fx-background-color: #9aaedc; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8;");
            }
        } else if ("booked".equalsIgnoreCase(status)) {
            actionBtn.setText("Booked");
            isClickable = false;
            actionBtn.setStyle("-fx-background-color: #6b6b6b; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8;");
        } else {
            actionBtn.setText(status);
            isClickable = false;
        }
        actionBtn.setDisable(!isClickable);

        // Action handler
        actionBtn.setOnAction(ev -> {
            // now uses direct LoginController.currentUser.getUserId()
            String cur = getCurrentUserId();
            if (cur == null || cur.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Please login");
                a.setHeaderText(null);
                a.setContentText("You must be logged in to request a booking.");
                a.showAndWait();
                return;
            }

            if ("available".equalsIgnoreCase(status)) {
                boolean ok = confirm("Request Booking", "Send booking request for room " + roomNumber + "?");
                if (ok) {
                    // store the userId (primary key string) in CSV
                    updateRoomBooking(roomId, "pending", cur);
                }
            } else if ("pending".equalsIgnoreCase(status)) {
                // cancel only if currentUser matches userId
                if (userId != null && userId.equals(cur)) {
                    boolean ok = confirm("Cancel Booking Request", "Cancel your booking request for room " + roomNumber + "?");
                    if (ok) {
                        updateRoomBooking(roomId, "available", "");
                    }
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Cannot cancel");
                    a.setHeaderText(null);
                    a.setContentText("Only the user who requested the booking can cancel it.");
                    a.showAndWait();
                }
            }
        });

        // assemble card
        card.getChildren().addAll(left, spacer, right, actionBtn);
        return card;
    }

    // helper to prompt confirmation
    private boolean confirm(String title, String message) {
        Alert c = new Alert(Alert.AlertType.CONFIRMATION);
        c.setTitle(title);
        c.setHeaderText(null);
        c.setContentText(message);
        Optional<ButtonType> res = c.showAndWait();
        return res.isPresent() && res.get() == ButtonType.OK;
    }

    // Update CSV row for a room: set status and userId, then reload
    private void updateRoomBooking(String roomId, String newStatus, String newUserId) {
        List<String[]> data = CSVManager.readCSV("rooms.csv");
        List<String[]> newData = new ArrayList<>();
        if (!data.isEmpty()) newData.add(data.get(0)); // header
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length < 7) {
                // try to normalize: expand to 7 fields
                String[] expanded = new String[7];
                for (int j = 0; j < expanded.length; j++) expanded[j] = j < row.length ? row[j] : "";
                row = expanded;
            }
            if (row[0].trim().equals(roomId)) {
                String[] updated = new String[row.length];
                System.arraycopy(row, 0, updated, 0, row.length);
                updated[5] = newStatus;
                updated[6] = newUserId;
                newData.add(updated);
            } else {
                newData.add(row);
            }
        }
        CSVManager.writeCSV("rooms.csv", newData);
        loadRooms();
    }

    // formatting & mapping helpers
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

    private String statusDisplay(String status) {
        if (status == null) return "Unknown";
        switch (status.toLowerCase()) {
            case "available": return "Available";
            case "pending": return "Pending";
            case "booked": return "Booked";
            default: return status;
        }
    }

    private String statusColor(String status) {
        if (status == null) return "#bdbdbd";
        switch (status.toLowerCase()) {
            case "available": return "#4ade80"; // light green
            case "pending": return "#ffb86b";   // yellowish-orange
            case "booked": return "#ff6b6b";    // red
            default: return "#bdbdbd";
        }
    }

    // Direct current-user retrieval using your User model
    private String getCurrentUserId() {
        try {
            if (LoginController.currentUser == null) return null;
            return LoginController.currentUser.getUserId();
        } catch (Throwable t) {
            return null;
        }
    }
}