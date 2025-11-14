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

public class AddRoomController {

    public static String parentHotelId = null;
    public static String parentHotelName = null;

    @FXML private TextField roomNumberField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField priceField;

    private final Map<String, String> types = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        types.put("Single", "single");
        types.put("Double", "double");
        types.put("Deluxe Suite", "deluxe");

        typeCombo.getItems().addAll(types.keySet());
        typeCombo.setValue("Single");
    }

    @FXML
    public void saveRoom() {
        String roomNumber = roomNumberField.getText().trim();
        String displayType = typeCombo.getValue();
        String typeCode = types.getOrDefault(displayType, "single");
        String priceRaw = priceField.getText().trim();

        if (parentHotelId == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText("No hotel selected.");
            a.showAndWait();
            return;
        }

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

        // read existing and check uniqueness within hotel
        List<String[]> data = CSVManager.readCSV("rooms.csv");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length >= 3) {
                String hid = row[1].trim();
                String rn = row[2].trim();
                if (hid.equals(parentHotelId) && rn.equalsIgnoreCase(roomNumber)) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Duplicate");
                    a.setHeaderText(null);
                    a.setContentText("Room number already exists for this hotel.");
                    a.showAndWait();
                    return;
                }
            }
        }

        // generate new roomId
        int newId = 1;
        if (data.size() > 1) {
            try {
                String lastId = data.get(data.size()-1)[0];
                newId = Integer.parseInt(lastId) + 1;
            } catch (Exception ex) {
                newId = data.size();
            }
        }

        // ensure header kept, then append new row with empty userId
        List<String[]> newData = new ArrayList<>();
        if (!data.isEmpty()) newData.add(data.get(0));
        newData.addAll(data.subList(1, data.size()));

        String[] newRow = { String.valueOf(newId), parentHotelId, roomNumber, typeCode, priceNormalized, "available", "" };
        newData.add(newRow);

        CSVManager.writeCSV("rooms.csv", newData);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Saved");
        info.setHeaderText(null);
        info.setContentText("Room added successfully.");
        info.showAndWait();

        SceneManager.switchScene("adminRoomList.fxml", parentHotelName + " - Manage Rooms");
    }

    @FXML public void back() { SceneManager.goBack(); }
}