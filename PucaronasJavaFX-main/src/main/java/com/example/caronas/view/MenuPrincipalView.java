package com.example.caronas.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.view.usuario.UsuarioCrudView;
import com.example.caronas.view.perfil.PerfilCrudView;
import com.example.caronas.view.veiculo.VeiculoCrudView;
import com.example.caronas.view.motorista.MotoristaCrudView;
import com.example.caronas.view.carona.CaronaCrudView;
import com.example.caronas.view.solicitacao.SolicitacaoCaronaCrudView;
import com.example.caronas.view.pagamento.PagamentoCrudView;
import com.example.caronas.view.historico.HistoricoCrudView;
import com.example.caronas.view.avaliacao.AvaliacaoCrudView;
import com.example.caronas.view.notificacao.NotificacaoCrudView;

public class MenuPrincipalView {
    private Stage stage;
    private Scene cena;
    private BorderPane root;

    public MenuPrincipalView(Stage stage) {
        this.stage = stage;
        criarTela();
    }

    private void criarTela() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        Label titulo = new Label("Menu Principal  Sistema de Caronas Universitárias");
        titulo.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-padding: 10;");
        BorderPane.setAlignment(titulo, Pos.CENTER);
        root.setTop(titulo);


        VBox menuBox = criarMenu();
        root.setCenter(menuBox);

        cena = new Scene(root, 1000, 700);
    }

    private VBox criarMenu() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Button btnUsuarios = new Button(" Gerenciar Usuários");
        Button btnPerfis = new Button(" Gerenciar Perfis");
        Button btnVeiculos = new Button(" Gerenciar Veículos");
        Button btnMotoristas = new Button("️ Gerenciar Motoristas");
        Button btnCaronas = new Button(" Gerenciar Caronas");
        Button btnSolicitacoes = new Button(" Gerenciar Solicitações de Carona");
        Button btnPagamentos = new Button(" Gerenciar Pagamentos");
        Button btnHistorico = new Button(" Histórico de Corridas");
        Button btnAvaliacoes = new Button(" Gerenciar Avaliações");
        Button btnNotificacoes = new Button(" Gerenciar Notificações");

        // Definir estilo dos botões
        String btnStyle = "-fx-font-size: 14; -fx-padding: 10; -fx-min-height: 40;";
        btnUsuarios.setStyle(btnStyle);
        btnPerfis.setStyle(btnStyle);
        btnVeiculos.setStyle(btnStyle);
        btnMotoristas.setStyle(btnStyle);
        btnCaronas.setStyle(btnStyle);
        btnSolicitacoes.setStyle(btnStyle);
        btnPagamentos.setStyle(btnStyle);
        btnHistorico.setStyle(btnStyle);
        btnAvaliacoes.setStyle(btnStyle);
        btnNotificacoes.setStyle(btnStyle);

        // Ações dos botões
        btnUsuarios.setOnAction(e -> abrirTela(new UsuarioCrudView(stage, this)));
        btnPerfis.setOnAction(e -> abrirTela(new PerfilCrudView(stage, this)));
        btnVeiculos.setOnAction(e -> abrirTela(new VeiculoCrudView(stage, this)));
        btnMotoristas.setOnAction(e -> abrirTela(new MotoristaCrudView(stage, this)));
        btnCaronas.setOnAction(e -> abrirTela(new CaronaCrudView(stage, this)));
        btnSolicitacoes.setOnAction(e -> abrirTela(new SolicitacaoCaronaCrudView(stage, this)));
        btnPagamentos.setOnAction(e -> abrirTela(new PagamentoCrudView(stage, this)));
        btnHistorico.setOnAction(e -> abrirTela(new HistoricoCrudView(stage, this)));
        btnAvaliacoes.setOnAction(e -> abrirTela(new AvaliacaoCrudView(stage, this)));
        btnNotificacoes.setOnAction(e -> abrirTela(new NotificacaoCrudView(stage, this)));

        vbox.getChildren().addAll(
                btnUsuarios,
                btnPerfis,
                btnVeiculos,
                btnMotoristas,
                btnCaronas,
                btnSolicitacoes,
                btnPagamentos,
                btnHistorico,
                btnAvaliacoes,
                btnNotificacoes
        );

        return vbox;
    }

    private void abrirTela(BaseCrudView view) {
        stage.setScene(view.getScene());
    }

    public Scene getScene() {
        return cena;
    }
}

