package com.example.caronas.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class BaseCrudView {
    protected Stage stage;
    protected Scene scene;
    protected BorderPane root;
    protected MenuPrincipalView menuView;

    public BaseCrudView(Stage stage, MenuPrincipalView menuView) {
        this.stage = stage;
        this.menuView = menuView;
        inicializarController();
        criarTela();
    }

    protected abstract void inicializarController();
    protected abstract void criarTela();

    protected HBox criarBotaoVoltar() {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10));

        Button btnVoltar = new Button("← Voltar ao Menu");
        btnVoltar.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        btnVoltar.setOnAction(e -> stage.setScene(menuView.getScene()));

        hbox.getChildren().add(btnVoltar);
        return hbox;
    }

    public Scene getScene() {
        return scene;
    }
}