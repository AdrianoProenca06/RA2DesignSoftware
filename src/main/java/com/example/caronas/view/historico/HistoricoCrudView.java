package com.example.caronas.view.historico;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.HistoricoCorrridaController;
import com.example.caronas.model.HistoricoCorrida;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class HistoricoCrudView extends BaseCrudView {
    private HistoricoCorrridaController controller;
    private TableView<HistoricoCorrida> tabela;
    private TextField txtMotoristaId;
    private TextField txtPassageiroId;
    private TextField txtTrajeto;
    private TextField txtValor;
    private HistoricoCorrida historicSelecionado;

    public HistoricoCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new HistoricoCorrridaController();
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

        Label lblTitulo = new Label("Histórico de Corridas");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblMotoristaId = new Label("ID Motorista:");
        txtMotoristaId = new TextField();
        txtMotoristaId.setPromptText("ID do motorista");
        txtMotoristaId.setEditable(true);

        Label lblPassageiroId = new Label("ID Passageiro:");
        txtPassageiroId = new TextField();
        txtPassageiroId.setPromptText("ID do passageiro");
        txtPassageiroId.setEditable(true);

        Label lblTrajeto = new Label("Trajeto:");
        txtTrajeto = new TextField();
        txtTrajeto.setPromptText("Local A -> Local B");

        Label lblValor = new Label("Valor:");
        txtValor = new TextField();
        txtValor.setPromptText("ex: 20.00");
        txtValor.setEditable(true);

        HBox hboxBotoes = new HBox(10);
        hboxBotoes.setPadding(new Insets(10));

        Button btnSalvar = new Button(" Salvar");
        Button btnAtualizar = new Button(" Atualizar");
        Button btnExcluir = new Button(" Excluir");
        Button btnLimpar = new Button(" Limpar");

        btnSalvar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnAtualizar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnExcluir.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnLimpar.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        btnSalvar.setOnAction(e -> salvar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limpar());

        hboxBotoes.getChildren().addAll(btnSalvar, btnAtualizar, btnExcluir, btnLimpar);

        vbox.getChildren().addAll(
                lblTitulo,
                lblMotoristaId, txtMotoristaId,
                lblPassageiroId, txtPassageiroId,
                lblTrajeto, txtTrajeto,
                lblValor, txtValor,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<HistoricoCorrida> criarTabela() {
        TableView<HistoricoCorrida> tabela = new TableView<>();

        TableColumn<HistoricoCorrida, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<HistoricoCorrida, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getData().toString()));

        TableColumn<HistoricoCorrida, String> colMotoristaId = new TableColumn<>("Motorista ID");
        colMotoristaId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMotoristaId()));

        TableColumn<HistoricoCorrida, String> colPassageiroId = new TableColumn<>("Passageiro ID");
        colPassageiroId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassageiroId()));

        TableColumn<HistoricoCorrida, String> colTrajeto = new TableColumn<>("Trajeto");
        colTrajeto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTrajeto()));

        TableColumn<HistoricoCorrida, Double> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getValor()));

        tabela.getColumns().addAll(colId, colData, colMotoristaId, colPassageiroId, colTrajeto, colValor);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            historicSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (historicSelecionado != null) {
                txtMotoristaId.setText(historicSelecionado.getMotoristaId());
                txtPassageiroId.setText(historicSelecionado.getPassageiroId());
                txtTrajeto.setText(historicSelecionado.getTrajeto());
                txtValor.setText(String.valueOf(historicSelecionado.getValor()));
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtMotoristaId.getText().isEmpty() || txtPassageiroId.getText().isEmpty() || txtTrajeto.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            double valor = txtValor.getText().isEmpty() ? 0.0 : Double.parseDouble(txtValor.getText());

            controller.criar(
                    txtMotoristaId.getText(),
                    txtPassageiroId.getText(),
                    txtTrajeto.getText(),
                    valor
            );

            DialogUtil.mostrarInfo("Sucesso", "Histórico salvo com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (historicSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um histórico para atualizar!");
                return;
            }

            double valor = Double.parseDouble(txtValor.getText());
            controller.atualizar(
                    historicSelecionado.getId(),
                    txtMotoristaId.getText(),
                    txtPassageiroId.getText(),
                    txtTrajeto.getText(),
                    valor
            );

            DialogUtil.mostrarInfo("Sucesso", "Histórico atualizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (historicSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um histórico para excluir!");
                return;
            }

            controller.excluir(historicSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Histórico excluído!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtMotoristaId.clear();
        txtPassageiroId.clear();
        txtTrajeto.clear();
        txtValor.clear();
        historicSelecionado = null;
    }

    private void atualizarTabela() {
        ObservableList<HistoricoCorrida> historicos = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(historicos);
    }
}

