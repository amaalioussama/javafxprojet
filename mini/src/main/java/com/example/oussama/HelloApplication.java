package com.example.oussama;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

       Parent root = FXMLLoader.load(getClass().getResource("/com/example/oussama/login.fxml"));
        stage.initStyle(StageStyle.DECORATED);
      Scene scene = new Scene(root,530, 400);
       stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}