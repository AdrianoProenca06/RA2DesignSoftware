package com.example.caronas.view.motorista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.MotoristaController;
import com.example.caronas.model.Motorista;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class MotoristaCrudView extends BaseCrudView {
    private MotoristaController controller;
    private TableView<Motorista> tabela;
    private TextField txtUsuarioId;
    private TextField txtVeiculoId;
    private TextField txtCnh;
    private ComboBox<String> cbStatus;
    private TextField txtAvaliacao;
    private Motorista motoristaSelecionado;

    public MotoristaCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new MotoristaController();
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

        Label lblTitulo = new Label("Cadastro de Motoristas");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblUsuarioId = new Label("ID Usuário:");
        txtUsuarioId = new TextField();
        txtUsuarioId.setPromptText("ID do usuário");
        txtUsuarioId.setEditable(true);

        Label lblVeiculoId = new Label("ID Veículo:");
        txtVeiculoId = new TextField();
        txtVeiculoId.setPromptText("ID do veículo");

        Label lblCnh = new Label("CNH:");
        txtCnh = new TextField();
        txtCnh.setPromptText("Número da CNH");

        Label lblStatus = new Label("Status:");
        cbStatus = new ComboBox<>();
        cbStatus.setItems(FXCollections.observableArrayList("ativo", "inativo"));
        cbStatus.setPrefWidth(200);

        Label lblAvaliacao = new Label("Avaliação:");
        txtAvaliacao = new TextField();
        txtAvaliacao.setPromptText("ex: 4.5");
        txtAvaliacao.setEditable(true);

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
                lblUsuarioId, txtUsuarioId,
                lblVeiculoId, txtVeiculoId,
                lblCnh, txtCnh,
                lblStatus, cbStatus,
                lblAvaliacao, txtAvaliacao,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Motorista> criarTabela() {
        TableView<Motorista> tabela = new TableView<>();

        TableColumn<Motorista, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Motorista, String> colUsuarioId = new TableColumn<>("Usuário ID");
        colUsuarioId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuarioId()));

        TableColumn<Motorista, String> colVeiculoId = new TableColumn<>("Veículo ID");
        colVeiculoId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVeiculoId()));

        TableColumn<Motorista, String> colCnh = new TableColumn<>("CNH");
        colCnh.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCnh()));

        TableColumn<Motorista, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        TableColumn<Motorista, Double> colAvaliacao = new TableColumn<>("Avaliação");
        colAvaliacao.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAvaliacao()));

        tabela.getColumns().addAll(colId, colUsuarioId, colVeiculoId, colCnh, colStatus, colAvaliacao);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            motoristaSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (motoristaSelecionado != null) {
                txtUsuarioId.setText(motoristaSelecionado.getUsuarioId());
                txtVeiculoId.setText(motoristaSelecionado.getVeiculoId());
                txtCnh.setText(motoristaSelecionado.getCnh());
                cbStatus.setValue(motoristaSelecionado.getStatus());
                txtAvaliacao.setText(String.valueOf(motoristaSelecionado.getAvaliacao()));
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtUsuarioId.getText().isEmpty() || txtVeiculoId.getText().isEmpty() || txtCnh.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.criar(
                    txtUsuarioId.getText(),
                    txtVeiculoId.getText(),
                    txtCnh.getText()
            );

            DialogUtil.mostrarInfo("Sucesso", "Motorista salvo com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (motoristaSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um motorista para atualizar!");
                return;
            }

            controller.atualizar(
                    motoristaSelecionado.getId(),
                    txtUsuarioId.getText(),
                    txtVeiculoId.getText(),
                    txtCnh.getText(),
                    cbStatus.getValue() != null ? cbStatus.getValue() : "ativo",
                    motoristaSelecionado.getAvaliacao()
            );

            DialogUtil.mostrarInfo("Sucesso", "Motorista atualizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (motoristaSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um motorista para excluir!");
                return;
            }

            controller.excluir(motoristaSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Motorista excluído com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtUsuarioId.clear();
        txtVeiculoId.clear();
        txtCnh.clear();
        cbStatus.setValue(null);
        txtAvaliacao.clear();
        motoristaSelecionado = null;
    }

    private void atualizarTabela() {
        ObservableList<Motorista> motoristas = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(motoristas);
    }
}

