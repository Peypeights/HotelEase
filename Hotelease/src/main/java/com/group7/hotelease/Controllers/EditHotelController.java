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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EditHotelController {
    public static String selectedHotelId = null;

    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField locationField;
    @FXML private TextArea descArea;

    // displayName -> shortCode
    private final Map<String, String> types = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        types.put("Coastal Bliss", "coastal");
        types.put("Highland Haven", "highland");
        types.put("Urban Escape", "urban");

        typeCombo.getItems().addAll(types.keySet());

        if (selectedHotelId != null) loadHotelData();
    }

    private void loadHotelData() {
        List<String[]> data = CSVManager.readCSV("hotels.csv");
        for (int i = 1; i < data.size(); i++) {
            String[] r = data.get(i);
            if (r.length > 0 && r[0].trim().equals(selectedHotelId)) {
                nameField.setText(r[1]);
                String shortCode = r[2];
                String display = mapCodeToDisplay(shortCode);
                typeCombo.setValue(display != null ? display : "Coastal Bliss");
                locationField.setText(r[3]);
                descArea.setText(r[4]);
                return;
            }
        }
    }

    private String mapCodeToDisplay(String code) {
        for (Map.Entry<String, String> e : types.entrySet()) {
            if (e.getValue().equalsIgnoreCase(code)) return e.getKey();
        }
        return null;
    }

    @FXML
    public void saveChanges() {
        if (selectedHotelId == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText("No hotel selected.");
            a.showAndWait();
            return;
        }

        String name = nameField.getText().trim();
        String displayType = typeCombo.getValue();
        String type = types.getOrDefault(displayType, "coastal");
        String location = locationField.getText().trim();
        String desc = descArea.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Validation");
            a.setHeaderText(null);
            a.setContentText("Please fill in required fields.");
            a.showAndWait();
            return;
        }

        List<String[]> data = CSVManager.readCSV("hotels.csv");
        List<String[]> newData = new ArrayList<>();
        if (!data.isEmpty()) newData.add(data.get(0)); // header

        for (int i = 1; i < data.size(); i++) {
            String[] r = data.get(i);
            if (r.length > 0 && r[0].trim().equals(selectedHotelId)) {
                String[] updated = { selectedHotelId, name, type, location, desc };
                newData.add(updated);
            } else {
                newData.add(r);
            }
        }

        CSVManager.writeCSV("hotels.csv", newData);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Saved");
        info.setHeaderText(null);
        info.setContentText("Hotel updated.");
        info.showAndWait();

        selectedHotelId = null;
        SceneManager.switchScene("manageHotels.fxml", "Manage Hotels");
    }

    @FXML public void back() {
        selectedHotelId = null;
        SceneManager.goBack();
    }
}