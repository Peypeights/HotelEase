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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddHotelController {
    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField locationField;
    @FXML private TextArea descArea;

    // displayName -> shortCode (keeps insertion order)
    private final Map<String, String> types = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        // Friendly display names mapped to short CSV codes
        types.put("Coastal Bliss", "coastal");
        types.put("Highland Haven", "highland");
        types.put("Urban Escape", "urban");

        // populate combo with friendly names
        typeCombo.getItems().addAll(types.keySet());
        typeCombo.setValue("Coastal Bliss");
    }

    @FXML
    public void saveHotel() {
        String name = nameField.getText().trim();
        String displayType = typeCombo.getValue();
        String type = types.getOrDefault(displayType, "coastal"); // short code to save
        String location = locationField.getText().trim();
        String desc = descArea.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Validation");
            a.setHeaderText(null);
            a.setContentText("Please fill in the required fields.");
            a.showAndWait();
            return;
        }

        List<String[]> data = CSVManager.readCSV("hotels.csv");
        int newId = 1;
        if (data.size() > 1) {
            try {
                String lastId = data.get(data.size() - 1)[0];
                newId = Integer.parseInt(lastId) + 1;
            } catch (Exception ex) {
                newId = data.size();
            }
        }
        String[] newRow = { String.valueOf(newId), name, type, location, desc };
        CSVManager.appendCSV("hotels.csv", newRow);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Saved");
        info.setHeaderText(null);
        info.setContentText("Hotel added successfully.");
        info.showAndWait();

        SceneManager.switchScene("manageHotels.fxml", "Manage Hotels");
    }

    @FXML public void back() { SceneManager.goBack(); }
}