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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminBookingsController {
    @FXML private VBox bookingsContainer;

    @FXML
    public void initialize() {
        loadBookings();
    }

    private void loadBookings() {
        bookingsContainer.getChildren().clear();

        List<String[]> bookings = CSVManager.readCSV("bookings.csv");
        if (bookings.size() <= 1) {
            Label none = new Label("No bookings found.");
            none.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 14px;");
            bookingsContainer.getChildren().add(none);
            return;
        }

        // load other CSVs for lookup
        List<String[]> users = CSVManager.readCSV("users.csv");
        List<String[]> rooms = CSVManager.readCSV("rooms.csv");
        List<String[]> hotels = CSVManager.readCSV("hotels.csv");

        for (int i = 1; i < bookings.size(); i++) {
            String[] b = bookings.get(i);
            if (b.length < 6) continue;
            String bookingId = b[0];
            String userId = b[1];
            String roomId = b[2];
            String checkIn = b[3];
            String checkOut = b[4];
            String status = b[5];

            // find username
            String username = lookupUsername(users, userId);
            // find room number and hotel id
            String roomNumber = lookupRoomNumber(rooms, roomId);
            String hotelId = lookupHotelIdForRoom(rooms, roomId);
            String hotelName = lookupHotelName(hotels, hotelId);

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12));
            row.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;");

            VBox left = new VBox(6);
            Label title = new Label("Booking #" + bookingId + " — Room " + roomNumber + " @ " + hotelName);
            title.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");
            Label userLine = new Label("User: " + userId + " (" + username + ")");
            userLine.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
            Label dates = new Label("From: " + checkIn + " — To: " + checkOut);
            dates.setStyle("-fx-text-fill: #b0b0b0; -fx-font-size: 12px;");
            left.getChildren().addAll(title, userLine, dates);

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            HBox actions = new HBox(8);
            actions.setAlignment(Pos.CENTER_RIGHT);

            Button accept = new Button("✓");
            accept.setPrefWidth(44);
            accept.setStyle("-fx-background-color: #4ade80; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
            Button reject = new Button("✕");
            reject.setPrefWidth(44);
            reject.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");

            // Accept/reject only enabled for pending bookings
            boolean isPending = "pending".equalsIgnoreCase(status);
            accept.setDisable(!isPending);
            reject.setDisable(!isPending);

            accept.setOnAction(e -> {
                boolean ok = confirm("Accept Booking", "Accept booking #" + bookingId + " ?");
                if (ok) {
                    // set booking.status -> accepted
                    updateBookingStatus(bookingId, "accepted");
                    // mark room as booked and ensure userId on room is set
                    updateRoomStatusById(roomId, "booked", userId);
                    loadBookings();
                }
            });

            reject.setOnAction(e -> {
                boolean ok = confirm("Reject Booking", "Reject booking #" + bookingId + " ?");
                if (ok) {
                    updateBookingStatus(bookingId, "rejected");
                    // set room back to available (clear userId)
                    updateRoomStatusById(roomId, "available", "");
                    loadBookings();
                }
            });

            actions.getChildren().addAll(accept, reject);

            row.getChildren().addAll(left, spacer, actions);
            bookingsContainer.getChildren().add(row);
        }
    }

    private String lookupUsername(List<String[]> users, String userId) {
        for (int i = 1; i < users.size(); i++) {
            String[] u = users.get(i);
            if (u.length >= 2 && u[0].equals(userId)) return u[1];
        }
        return "Unknown";
    }

    private String lookupRoomNumber(List<String[]> rooms, String roomId) {
        for (int i = 1; i < rooms.size(); i++) {
            String[] r = rooms.get(i);
            if (r.length >= 3 && r[0].equals(roomId)) return r[2];
        }
        return "N/A";
    }

    private String lookupHotelIdForRoom(List<String[]> rooms, String roomId) {
        for (int i = 1; i < rooms.size(); i++) {
            String[] r = rooms.get(i);
            if (r.length >= 2 && r[0].equals(roomId)) return r[1];
        }
        return "";
    }

    private String lookupHotelName(List<String[]> hotels, String hotelId) {
        for (int i = 1; i < hotels.size(); i++) {
            String[] h = hotels.get(i);
            if (h.length >= 2 && h[0].equals(hotelId)) return h[1];
        }
        return "Unknown Hotel";
    }

    private void updateBookingStatus(String bookingId, String newStatus) {
        List<String[]> rows = CSVManager.readCSV("bookings.csv");
        List<String[]> out = new ArrayList<>();
        if (!rows.isEmpty()) out.add(rows.get(0));
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length >= 1 && r[0].equals(bookingId)) {
                String[] updated = new String[Math.max(6, r.length)];
                for (int j = 0; j < updated.length; j++) updated[j] = j < r.length ? r[j] : "";
                updated[5] = newStatus;
                out.add(updated);
            } else {
                if (r.length < 6) {
                    String[] ex = new String[6];
                    for (int j = 0; j < 6; j++) ex[j] = j < r.length ? r[j] : "";
                    out.add(ex);
                } else out.add(r);
            }
        }
        CSVManager.writeCSV("bookings.csv", out);
    }

    private void updateRoomStatusById(String roomId, String newStatus, String newUserId) {
        List<String[]> rows = CSVManager.readCSV("rooms.csv");
        List<String[]> out = new ArrayList<>();
        if (!rows.isEmpty()) out.add(rows.get(0));
        for (int i = 1; i < rows.size(); i++) {
            String[] r = rows.get(i);
            if (r.length < 7) {
                String[] ex = new String[7];
                for (int j = 0; j < 7; j++) ex[j] = j < r.length ? r[j] : "";
                r = ex;
            }
            if (r[0].equals(roomId)) {
                String[] updated = new String[r.length];
                System.arraycopy(r, 0, updated, 0, r.length);
                updated[5] = newStatus;
                updated[6] = newUserId;
                out.add(updated);
            } else {
                out.add(r);
            }
        }
        CSVManager.writeCSV("rooms.csv", out);
    }

    private boolean confirm(String title, String message) {
        Alert c = new Alert(Alert.AlertType.CONFIRMATION);
        c.setTitle(title);
        c.setHeaderText(null);
        c.setContentText(message);
        Optional<ButtonType> res = c.showAndWait();
        return res.isPresent() && res.get() == ButtonType.OK;
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
