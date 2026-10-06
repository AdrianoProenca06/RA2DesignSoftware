package com.example.caronas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.event.AuditoriaObserver;
import com.example.caronas.event.BarramentoEventos;
import com.example.caronas.event.HistoricoObserver;
import com.example.caronas.event.NotificacaoObserver;
import com.example.caronas.service.HistoricoCorrridaService;
import com.example.caronas.service.MotoristaService;
import com.example.caronas.service.NotificacaoService;
import com.example.caronas.view.MenuPrincipalView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        registrarObservadores();
        MenuPrincipalView menuView = new MenuPrincipalView(stage);
        stage.setScene(menuView.getScene());
        stage.setTitle("Sistema de Caronas Universitárias");
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.show();
    }

    /** Composição do Observer: quem reage aos eventos do domínio é definido aqui, em um único lugar. */
    private void registrarObservadores() {
        BarramentoEventos barramento = BarramentoEventos.getInstancia();
        barramento.registrar(new NotificacaoObserver(new NotificacaoService(), new MotoristaService()));
        barramento.registrar(new HistoricoObserver(new HistoricoCorrridaService()));
        barramento.registrar(new AuditoriaObserver());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

