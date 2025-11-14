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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EditRoomController {

    public static String selectedRoomId = null;
    public static String parentHotelId = null;

    @FXML private TextField roomNumberField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField priceField;

    private final Map<String,String> types = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        types.put("Single", "single");
        types.put("Double", "double");
        types.put("Deluxe Suite", "deluxe");

        typeCombo.getItems().addAll(types.keySet());

        if (selectedRoomId != null) loadRoomData();
    }

    private void loadRoomData() {
        List<String[]> data = CSVManager.readCSV("rooms.csv");
        for (int i = 1; i < data.size(); i++) {
            String[] r = data.get(i);
            if (r.length >= 7 && r[0].trim().equals(selectedRoomId)) {
                roomNumberField.setText(r[2]);
                String code = r[3];
                String display = mapCodeToDisplay(code);
                typeCombo.setValue(display != null ? display : "Single");
                priceField.setText(r[4]);
                return;
            }
        }
    }

    private String mapCodeToDisplay(String code) {
        for (Map.Entry<String,String> e : types.entrySet()) {
            if (e.getValue().equalsIgnoreCase(code)) return e.getKey();
        }
        return null;
    }

    @FXML
    public void saveChanges() {
        if (selectedRoomId == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText("No room selected.");
            a.showAndWait();
            return;
        }

        String roomNumber = roomNumberField.getText().trim();
        String displayType = typeCombo.getValue();
        String typeCode = types.getOrDefault(displayType, "single");
        String priceRaw = priceField.getText().trim();

        if (roomNumber.isEmpty() || priceRaw.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Validation");
            a.setHeaderText(null);
            a.setContentText("Please fill in required fields.");
            a.showAndWait();
            return;
        }

        String priceNormalized = priceRaw.replaceAll("[^0-9.]", "");
        if (priceNormalized.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Price error");
            a.setHeaderText(null);
            a.setContentText("Invalid price.");
            a.showAndWait();
            return;
        }

        // enforce uniqueness within hotel (except this room)
        List<String[]> data = CSVManager.readCSV("rooms.csv");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length >= 3) {
                String hid = row[1].trim();
                String rn = row[2].trim();
                String rid = row[0].trim();
                if (hid.equals(parentHotelId) && rn.equalsIgnoreCase(roomNumber) && !rid.equals(selectedRoomId)) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Duplicate");
                    a.setHeaderText(null);
                    a.setContentText("Room number already exists for this hotel.");
                    a.showAndWait();
                    return;
                }
            }
        }

        // rewrite csv with updated row, preserving userId (index 6)
        List<String[]> old = CSVManager.readCSV("rooms.csv");
        List<String[]> newData = new ArrayList<>();
        if (!old.isEmpty()) newData.add(old.get(0));
        for (int i = 1; i < old.size(); i++) {
            String[] row = old.get(i);
            if (row[0].trim().equals(selectedRoomId)) {
                String userId = row.length >= 7 ? row[6] : "";
                String[] updated = { selectedRoomId, parentHotelId, roomNumber, typeCode, priceNormalized, row.length >= 6 ? row[5] : "available", userId };
                newData.add(updated);
            } else {
                // normalize row length to at least 7 to avoid breakage
                if (row.length < 7) {
                    String[] expanded = new String[7];
                    for (int j = 0; j < 7; j++) expanded[j] = j < row.length ? row[j] : "";
                    newData.add(expanded);
                } else {
                    newData.add(row);
                }
            }
        }

        CSVManager.writeCSV("rooms.csv", newData);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Saved");
        info.setHeaderText(null);
        info.setContentText("Room updated.");
        info.showAndWait();

        selectedRoomId = null;
        SceneManager.switchScene("adminRoomList.fxml", RoomListController.selectedHotelName + " - Manage Rooms");
    }

    @FXML public void back() {
        selectedRoomId = null;
        SceneManager.goBack();
    }
}