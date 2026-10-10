package com.example.caronas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.view.MenuPrincipalView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MenuPrincipalView menuView = new MenuPrincipalView(stage);
        stage.setScene(menuView.getScene());
        stage.setTitle("Sistema de Caronas Universitárias");
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

