/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group7.hotelease.Utils;

/**
 *
 * @author lapid
 */
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Stack;

public class SceneManager {
    private static Stage stage;
    private static Stack<String> sceneHistory = new Stack<>();
    private static String currentScene;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlFile, String title) {
        try {
            if (currentScene != null) {
                sceneHistory.push(currentScene);
            }
            
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // ESC key handler
            scene.setOnKeyPressed(event -> {
                if (event.getCode().toString().equals("ESCAPE")) {
                    goBack();
                }
            });
            
            stage.setScene(scene);
            stage.setTitle(title);
            currentScene = fxmlFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void goBack() {
        if (!sceneHistory.isEmpty()) {
            String previousScene = sceneHistory.pop();
            currentScene = null; // Prevent adding to history again
            switchScene(previousScene, "Hotel Booking System");
        }
    }

    public static void clearHistory() {
        sceneHistory.clear();
    }
}