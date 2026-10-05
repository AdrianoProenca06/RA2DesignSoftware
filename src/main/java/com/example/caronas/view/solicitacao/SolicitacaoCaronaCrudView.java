package com.example.caronas.view.solicitacao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.SolicitacaoCaronaController;
import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class SolicitacaoCaronaCrudView extends BaseCrudView {
    private SolicitacaoCaronaController controller;
    private TableView<SolicitacaoCarona> tabela;
    private TextField txtPassageiroId;
    private TextField txtCaronaId;
    private ComboBox<String> cbStatus;
    private SolicitacaoCarona solicitacaoSelecionada;

    public SolicitacaoCaronaCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new SolicitacaoCaronaController();
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

        Label lblTitulo = new Label("Gerenciar Solicitações de Carona");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblPassageiroId = new Label("ID Passageiro:");
        txtPassageiroId = new TextField();
        txtPassageiroId.setPromptText("ID do passageiro");
        txtPassageiroId.setEditable(true);

        Label lblCaronaId = new Label("ID Carona:");
        txtCaronaId = new TextField();
        txtCaronaId.setPromptText("ID da carona");
        txtCaronaId.setEditable(true);

        Label lblStatus = new Label("Status:");
        cbStatus = new ComboBox<>();
        cbStatus.setItems(FXCollections.observableArrayList("pendente", "aceita", "recusada"));
        cbStatus.setPrefWidth(200);

        HBox hboxBotoes = new HBox(10);
        hboxBotoes.setPadding(new Insets(10));

        Button btnAceitar = new Button(" Salvar");
        Button btnAtualizar = new Button(" Atualizar");
        Button btnRecusar = new Button(" Recusar");
        Button btnExcluir = new Button(" Excluir");
        Button btnLimpar = new Button(" Limpar");

        btnAceitar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnAtualizar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnRecusar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnExcluir.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnLimpar.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        btnAceitar.setOnAction(e -> salvar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnRecusar.setOnAction(e -> recusar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limpar());

        hboxBotoes.getChildren().addAll(btnAceitar, btnAtualizar, btnRecusar, btnExcluir, btnLimpar);

        vbox.getChildren().addAll(
                lblTitulo,
                lblPassageiroId, txtPassageiroId,
                lblCaronaId, txtCaronaId,
                lblStatus, cbStatus,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<SolicitacaoCarona> criarTabela() {
        TableView<SolicitacaoCarona> tabela = new TableView<>();

        TableColumn<SolicitacaoCarona, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<SolicitacaoCarona, String> colPassageiroId = new TableColumn<>("Passageiro ID");
        colPassageiroId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPassageiroId()));

        TableColumn<SolicitacaoCarona, String> colCaronaId = new TableColumn<>("Carona ID");
        colCaronaId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCaronaId()));

        TableColumn<SolicitacaoCarona, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        TableColumn<SolicitacaoCarona, String> colData = new TableColumn<>("Data Solicitação");
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDataSolicitacao().toString()));

        tabela.getColumns().addAll(colId, colPassageiroId, colCaronaId, colStatus, colData);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            solicitacaoSelecionada = tabela.getSelectionModel().getSelectedItem();
            if (solicitacaoSelecionada != null) {
                txtPassageiroId.setText(solicitacaoSelecionada.getPassageiroId());
                txtCaronaId.setText(solicitacaoSelecionada.getCaronaId());
                cbStatus.setValue(solicitacaoSelecionada.getStatus());
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            String passageiroId = txtPassageiroId.getText();
            String caronaId = txtCaronaId.getText();

            if (passageiroId.isEmpty() || caronaId.isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos!");
                return;
            }

            controller.criar(passageiroId, caronaId);
            DialogUtil.mostrarInfo("Sucesso", "Solicitação salva!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (solicitacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma solicitação para atualizar!");
                return;
            }

            String passageiroId = txtPassageiroId.getText();
            String caronaId = txtCaronaId.getText();
            String status = cbStatus.getValue();

            if (passageiroId.isEmpty() || caronaId.isEmpty() || status == null) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos!");
                return;
            }

            controller.atualizar(
                    solicitacaoSelecionada.getId(),
                    passageiroId,
                    caronaId,
                    status
            );

            DialogUtil.mostrarInfo("Sucesso", "Solicitação atualizada com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void recusar() {
        try {
            if (solicitacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma solicitação para recusar!");
                return;
            }

            controller.recusar(solicitacaoSelecionada.getId());
            DialogUtil.mostrarInfo("Sucesso", "Solicitação recusada!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (solicitacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma solicitação para excluir!");
                return;
            }

            controller.excluir(solicitacaoSelecionada.getId());
            DialogUtil.mostrarInfo("Sucesso", "Solicitação excluída!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtPassageiroId.clear();
        txtCaronaId.clear();
        cbStatus.setValue(null);
        solicitacaoSelecionada = null;
    }

    private void atualizarTabela() {
        ObservableList<SolicitacaoCarona> solicitacoes = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(solicitacoes);
    }
}

