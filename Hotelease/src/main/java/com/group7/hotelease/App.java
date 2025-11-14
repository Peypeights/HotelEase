package com.group7.hotelease;

import com.group7.hotelease.Utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        SceneManager.switchScene("login.fxml", "HotelEase");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}   