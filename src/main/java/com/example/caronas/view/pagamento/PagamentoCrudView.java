package com.example.caronas.view.pagamento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.PagamentoController;
import com.example.caronas.model.Pagamento;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class PagamentoCrudView extends BaseCrudView {
    private PagamentoController controller;
    private TableView<Pagamento> tabela;
    private TextField txtSolicitacaoId;
    private TextField txtValor;
    private ComboBox<String> cbStatus;
    private ComboBox<String> cbMetodo;
    private Pagamento pagamentoSelecionado;

    public PagamentoCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new PagamentoController();
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

        Label lblTitulo = new Label("Gerenciar Pagamentos");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblSolicitacaoId = new Label("ID Solicitação:");
        txtSolicitacaoId = new TextField();
        txtSolicitacaoId.setPromptText("ID da solicitação");

        Label lblValor = new Label("Valor:");
        txtValor = new TextField();
        txtValor.setPromptText("ex: 50.00");

        Label lblStatus = new Label("Status:");
        cbStatus = new ComboBox<>();
        cbStatus.setItems(FXCollections.observableArrayList("pendente", "pago", "cancelado"));
        cbStatus.setPrefWidth(200);

        Label lblMetodo = new Label("Método:");
        cbMetodo = new ComboBox<>();
        cbMetodo.setItems(FXCollections.observableArrayList("pix", "dinheiro"));
        cbMetodo.setPrefWidth(200);

        HBox hboxBotoes = new HBox(10);
        hboxBotoes.setPadding(new Insets(10));

        Button btnSalvar = new Button(" Salvar");
        Button btnAtualizar = new Button(" Atualizar");
        Button btnPagar = new Button(" Pagar");
        Button btnCancelar = new Button(" Cancelar");
        Button btnExcluir = new Button(" Excluir");
        Button btnLimpar = new Button(" Limpar");

        btnSalvar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnAtualizar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnPagar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnCancelar.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnExcluir.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        btnLimpar.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        btnSalvar.setOnAction(e -> salvar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnPagar.setOnAction(e -> pagar());
        btnCancelar.setOnAction(e -> cancelar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limpar());

        hboxBotoes.getChildren().addAll(btnSalvar, btnAtualizar, btnPagar, btnCancelar, btnExcluir, btnLimpar);

        vbox.getChildren().addAll(
                lblTitulo,
                lblSolicitacaoId, txtSolicitacaoId,
                lblValor, txtValor,
                lblStatus, cbStatus,
                lblMetodo, cbMetodo,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Pagamento> criarTabela() {
        TableView<Pagamento> tabela = new TableView<>();

        TableColumn<Pagamento, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Pagamento, String> colSolicitacaoId = new TableColumn<>("Solicitação ID");
        colSolicitacaoId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSolicitacaoCaronaId()));

        TableColumn<Pagamento, Double> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getValor()));

        TableColumn<Pagamento, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        TableColumn<Pagamento, String> colMetodo = new TableColumn<>("Método");
        colMetodo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMetodo()));

        tabela.getColumns().addAll(colId, colSolicitacaoId, colValor, colStatus, colMetodo);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            pagamentoSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (pagamentoSelecionado != null) {
                txtSolicitacaoId.setText(pagamentoSelecionado.getSolicitacaoCaronaId());
                txtValor.setText(String.valueOf(pagamentoSelecionado.getValor()));
                cbStatus.setValue(pagamentoSelecionado.getStatus());
                cbMetodo.setValue(pagamentoSelecionado.getMetodo());
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtSolicitacaoId.getText().isEmpty() || txtValor.getText().isEmpty() || cbMetodo.getValue() == null) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            double valor = Double.parseDouble(txtValor.getText());
            controller.criar(
                    txtSolicitacaoId.getText(),
                    valor,
                    cbMetodo.getValue()
            );

            DialogUtil.mostrarInfo("Sucesso", "Pagamento salvo com sucesso!");
            limpar();
            atualizarTabela();
        } catch (NumberFormatException e) {
            DialogUtil.mostrarErro("Erro", "Valor deve ser um número decimal!");
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (pagamentoSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um pagamento para atualizar!");
                return;
            }

            if (txtSolicitacaoId.getText().isEmpty() || txtValor.getText().isEmpty() || cbMetodo.getValue() == null) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            double valor = Double.parseDouble(txtValor.getText());
            controller.atualizar(
                    pagamentoSelecionado.getId(),
                    txtSolicitacaoId.getText(),
                    valor,
                    cbMetodo.getValue(),
                    cbStatus.getValue() != null ? cbStatus.getValue() : "pendente"
            );

            DialogUtil.mostrarInfo("Sucesso", "Pagamento atualizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (NumberFormatException e) {
            DialogUtil.mostrarErro("Erro", "Valor deve ser um número decimal!");
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void pagar() {
        try {
            if (pagamentoSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um pagamento para realizar!");
                return;
            }

            controller.realizarPagamento(pagamentoSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Pagamento realizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void cancelar() {
        try {
            if (pagamentoSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um pagamento para cancelar!");
                return;
            }

            controller.cancelarPagamento(pagamentoSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Pagamento cancelado!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (pagamentoSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um pagamento para excluir!");
                return;
            }

            controller.excluir(pagamentoSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Pagamento excluído!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtSolicitacaoId.clear();
        txtValor.clear();
        cbStatus.setValue(null);
        cbMetodo.setValue(null);
        pagamentoSelecionado = null;
    }

    private void atualizarTabela() {
        ObservableList<Pagamento> pagamentos = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(pagamentos);
    }
}

