package com.example.caronas.view.notificacao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.NotificacaoController;
import com.example.caronas.model.Notificacao;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class NotificacaoCrudView extends BaseCrudView {
    private NotificacaoController controller;
    private TableView<Notificacao> tabela;
    private TextField txtUsuarioId;
    private TextArea txtMensagem;
    private CheckBox cbLida;
    private Notificacao notificacaoSelecionada;

    public NotificacaoCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new NotificacaoController();
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

        Label lblTitulo = new Label("Gerenciar Notificações");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblUsuarioId = new Label("ID Usuário:");
        txtUsuarioId = new TextField();
        txtUsuarioId.setPromptText("ID do usuário");

        Label lblMensagem = new Label("Mensagem:");
        txtMensagem = new TextArea();
        txtMensagem.setPrefHeight(80);
        txtMensagem.setWrapText(true);

        cbLida = new CheckBox("Marcada como Lida");

        HBox hboxBotoes = new HBox(10);
        hboxBotoes.setPadding(new Insets(10));

        Button btnSalvar = new Button(" Salvar");
        Button btnAtualizar = new Button(" Atualizar");
        Button btnMarcarLida = new Button(" Marcar como Lida");
        Button btnExcluir = new Button(" Excluir");
        Button btnLimpar = new Button(" Limpar");

        btnSalvar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnAtualizar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnMarcarLida.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnExcluir.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnLimpar.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        btnSalvar.setOnAction(e -> salvar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnMarcarLida.setOnAction(e -> marcarLida());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limpar());

        hboxBotoes.getChildren().addAll(btnSalvar, btnAtualizar, btnMarcarLida, btnExcluir, btnLimpar);

        vbox.getChildren().addAll(
                lblTitulo,
                lblUsuarioId, txtUsuarioId,
                lblMensagem, txtMensagem,
                cbLida,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Notificacao> criarTabela() {
        TableView<Notificacao> tabela = new TableView<>();

        TableColumn<Notificacao, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Notificacao, String> colUsuarioId = new TableColumn<>("Usuário ID");
        colUsuarioId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuarioId()));

        TableColumn<Notificacao, String> colMensagem = new TableColumn<>("Mensagem");
        colMensagem.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMensagem()));

        TableColumn<Notificacao, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getData().toString()));

        TableColumn<Notificacao, Boolean> colLida = new TableColumn<>("Lida");
        colLida.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().isLida()));

        tabela.getColumns().addAll(colId, colUsuarioId, colMensagem, colData, colLida);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            notificacaoSelecionada = tabela.getSelectionModel().getSelectedItem();
            if (notificacaoSelecionada != null) {
                txtUsuarioId.setText(notificacaoSelecionada.getUsuarioId());
                txtMensagem.setText(notificacaoSelecionada.getMensagem());
                cbLida.setSelected(notificacaoSelecionada.isLida());
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtUsuarioId.getText().isEmpty() || txtMensagem.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.criar(
                    txtUsuarioId.getText(),
                    txtMensagem.getText()
            );

            DialogUtil.mostrarInfo("Sucesso", "Notificação salva com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (notificacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma notificação para atualizar!");
                return;
            }

            if (txtUsuarioId.getText().isEmpty() || txtMensagem.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.atualizar(
                    notificacaoSelecionada.getId(),
                    txtUsuarioId.getText(),
                    txtMensagem.getText(),
                    cbLida.isSelected()
            );

            DialogUtil.mostrarInfo("Sucesso", "Notificação atualizada com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void marcarLida() {
        try {
            if (notificacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma notificação!");
                return;
            }

            controller.marcarComoLida(notificacaoSelecionada.getId());
            DialogUtil.mostrarInfo("Sucesso", "Notificação marcada como lida!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (notificacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma notificação para excluir!");
                return;
            }

            controller.excluir(notificacaoSelecionada.getId());
            DialogUtil.mostrarInfo("Sucesso", "Notificação excluída!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtUsuarioId.clear();
        txtMensagem.clear();
        cbLida.setSelected(false);
        notificacaoSelecionada = null;
    }

    private void atualizarTabela() {
        ObservableList<Notificacao> notificacoes = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(notificacoes);
    }
}

