package com.example.caronas.view.perfil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.PerfilController;
import com.example.caronas.model.Perfil;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class PerfilCrudView extends BaseCrudView {
    private PerfilController controller;
    private TableView<Perfil> tabela;
    private TextField txtUsuarioId;
    private TextField txtCurso;
    private TextField txtTurno;
    private TextField txtTelefone;
    private Perfil perfilSelecionado;

    public PerfilCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new PerfilController();
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

        Label lblTitulo = new Label("Cadastro de Perfis");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblUsuarioId = new Label("ID Usuário:");
        txtUsuarioId = new TextField();
        txtUsuarioId.setPromptText("ID do usuário");
        txtUsuarioId.setEditable(true);

        Label lblCurso = new Label("Curso:");
        txtCurso = new TextField();
        txtCurso.setPromptText("Digite o curso");

        Label lblTurno = new Label("Turno:");
        txtTurno = new TextField();
        txtTurno.setPromptText("ex: Manhã, Tarde, Noite");

        Label lblTelefone = new Label("Telefone:");
        txtTelefone = new TextField();
        txtTelefone.setPromptText("Digite o telefone");

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
                lblCurso, txtCurso,
                lblTurno, txtTurno,
                lblTelefone, txtTelefone,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Perfil> criarTabela() {
        TableView<Perfil> tabela = new TableView<>();

        TableColumn<Perfil, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Perfil, String> colUsuarioId = new TableColumn<>("Usuário ID");
        colUsuarioId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuarioId()));

        TableColumn<Perfil, String> colCurso = new TableColumn<>("Curso");
        colCurso.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCurso()));

        TableColumn<Perfil, String> colTurno = new TableColumn<>("Turno");
        colTurno.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTurno()));

        TableColumn<Perfil, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefone()));

        tabela.getColumns().addAll(colId, colUsuarioId, colCurso, colTurno, colTelefone);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            perfilSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (perfilSelecionado != null) {
                txtUsuarioId.setText(perfilSelecionado.getUsuarioId());
                txtCurso.setText(perfilSelecionado.getCurso());
                txtTurno.setText(perfilSelecionado.getTurno());
                txtTelefone.setText(perfilSelecionado.getTelefone());
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtUsuarioId.getText().isEmpty() || txtCurso.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.criar(
                    txtUsuarioId.getText(),
                    txtCurso.getText(),
                    txtTurno.getText(),
                    txtTelefone.getText()
            );

            DialogUtil.mostrarInfo("Sucesso", "Perfil salvo com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (perfilSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um perfil para atualizar!");
                return;
            }

            controller.atualizar(
                    perfilSelecionado.getId(),
                    txtCurso.getText(),
                    txtTurno.getText(),
                    txtTelefone.getText(),
                    perfilSelecionado.getAvaliacaoMedia()
            );

            DialogUtil.mostrarInfo("Sucesso", "Perfil atualizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (perfilSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um perfil para excluir!");
                return;
            }

            controller.excluir(perfilSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Perfil excluído com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtUsuarioId.clear();
        txtCurso.clear();
        txtTurno.clear();
        txtTelefone.clear();
        perfilSelecionado = null;
    }

    private void atualizarTabela() {
        ObservableList<Perfil> perfis = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(perfis);
    }
}

