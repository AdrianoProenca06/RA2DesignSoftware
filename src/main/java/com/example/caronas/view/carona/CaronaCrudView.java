package com.example.caronas.view.carona;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.CaronaController;
import com.example.caronas.model.Carona;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class CaronaCrudView extends BaseCrudView {
    private CaronaController controller;
    private TableView<Carona> tabela;
    private TextField txtMotoristaId;
    private TextField txtOrigem;
    private TextField txtDestino;
    private DatePicker dpData;
    private TextField txtHora;
    private TextField txtVagas;
    private Carona caronaSelecionada;

    public CaronaCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new CaronaController();
    }

    @Override
    protected void criarTela() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        VBox formulario = criarFormulario();
        tabela = criarTabela();

        VBox cont = new VBox(10);
        cont.getChildren().addAll(formulario, tabela);

        root.setCenter(cont);
        root.setBottom(criarBotaoVoltar());

        scene = new Scene(root, 1000, 700);
        atualizarTabela();
    }

    private VBox criarFormulario() {
        VBox vbox = new VBox(10);
        vbox.setBorder(new javafx.scene.layout.Border(new javafx.scene.layout.BorderStroke(
                javafx.scene.paint.Color.GRAY, javafx.scene.layout.BorderStrokeStyle.SOLID,
                null, null)));
        vbox.setPadding(new Insets(10));

        Label lblTitulo = new Label("Cadastro de Caronas");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblMotoristaId = new Label("ID Motorista:");
        txtMotoristaId = new TextField();
        txtMotoristaId.setPromptText("ID do motorista");

        Label lblOrigem = new Label("Origem:");
        txtOrigem = new TextField();
        txtOrigem.setPromptText("Local de saída");

        Label lblDestino = new Label("Destino:");
        txtDestino = new TextField();
        txtDestino.setPromptText("Local de chegada");

        Label lblData = new Label("Data:");
        dpData = new DatePicker();

        Label lblHora = new Label("Hora:");
        txtHora = new TextField();
        txtHora.setPromptText("HH:mm");

        Label lblVagas = new Label("Vagas Disponíveis:");
        txtVagas = new TextField();
        txtVagas.setPromptText("ex: 3");

        HBox hboxBotoes = new HBox(10);
        hboxBotoes.setPadding(new Insets(10));

        Button btnSalvar = new Button(" Salvar");
        Button btnLimpar = new Button(" Limpar");
        Button btnAtualizar = new Button(" Atualizar");
        Button btnExcluir = new Button(" Excluir");

        btnSalvar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnLimpar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnAtualizar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnExcluir.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        btnSalvar.setOnAction(e -> salvar());
        btnLimpar.setOnAction(e -> limpar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnExcluir.setOnAction(e -> excluir());

        hboxBotoes.getChildren().addAll(btnSalvar, btnLimpar, btnAtualizar, btnExcluir);

        vbox.getChildren().addAll(
                lblTitulo,
                lblMotoristaId, txtMotoristaId,
                lblOrigem, txtOrigem,
                lblDestino, txtDestino,
                lblData, dpData,
                lblHora, txtHora,
                lblVagas, txtVagas,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Carona> criarTabela() {
        TableView<Carona> tabela = new TableView<>();

        TableColumn<Carona, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Carona, String> colMotoristaId = new TableColumn<>("Motorista ID");
        colMotoristaId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMotoristaId()));

        TableColumn<Carona, String> colOrigem = new TableColumn<>("Origem");
        colOrigem.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrigem()));

        TableColumn<Carona, String> colDestino = new TableColumn<>("Destino");
        colDestino.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestino()));

        TableColumn<Carona, String> colHorario = new TableColumn<>("Horário");
        colHorario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHorario().toString()));

        TableColumn<Carona, Integer> colVagas = new TableColumn<>("Vagas");
        colVagas.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getVagasDisponiveis()));

        tabela.getColumns().addAll(colId, colMotoristaId, colOrigem, colDestino, colHorario, colVagas);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            caronaSelecionada = tabela.getSelectionModel().getSelectedItem();
            if (caronaSelecionada != null) {
                txtMotoristaId.setText(caronaSelecionada.getMotoristaId());
                txtOrigem.setText(caronaSelecionada.getOrigem());
                txtDestino.setText(caronaSelecionada.getDestino());
                dpData.setValue(caronaSelecionada.getHorario().toLocalDate());
                txtHora.setText(caronaSelecionada.getHorario().toLocalTime().toString());
                txtVagas.setText(String.valueOf(caronaSelecionada.getVagasDisponiveis()));
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtMotoristaId.getText().isEmpty() || txtOrigem.getText().isEmpty() || 
                txtDestino.getText().isEmpty() || dpData.getValue() == null || txtHora.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            int vagas = Integer.parseInt(txtVagas.getText());
            LocalDateTime horario = LocalDateTime.of(dpData.getValue(), LocalTime.parse(txtHora.getText()));

            controller.criar(
                    txtMotoristaId.getText(),
                    txtOrigem.getText(),
                    txtDestino.getText(),
                    horario,
                    vagas
            );

            DialogUtil.mostrarInfo("Sucesso", "Carona salva com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (caronaSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma carona para atualizar!");
                return;
            }

            int vagas = Integer.parseInt(txtVagas.getText());
            LocalDateTime horario = LocalDateTime.of(dpData.getValue(), LocalTime.parse(txtHora.getText()));

            controller.atualizar(
                    caronaSelecionada.getId(),
                    txtMotoristaId.getText(),
                    txtOrigem.getText(),
                    txtDestino.getText(),
                    horario,
                    vagas
            );

            DialogUtil.mostrarInfo("Sucesso", "Carona atualizada com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (caronaSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma carona para excluir!");
                return;
            }

            controller.excluir(caronaSelecionada.getId());
            DialogUtil.mostrarInfo("Sucesso", "Carona excluída com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtMotoristaId.clear();
        txtOrigem.clear();
        txtDestino.clear();
        dpData.setValue(null);
        txtHora.clear();
        txtVagas.clear();
        caronaSelecionada = null;
    }

    private void atualizarTabela() {
        ObservableList<Carona> caronas = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(caronas);
    }
}

