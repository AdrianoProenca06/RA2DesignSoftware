package com.example.caronas.view.veiculo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.VeiculoController;
import com.example.caronas.model.Veiculo;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class VeiculoCrudView extends BaseCrudView {
    private VeiculoController controller;
    private TableView<Veiculo> tabela;
    private TextField txtModelo;
    private TextField txtPlaca;
    private TextField txtCor;
    private TextField txtCapacidade;
    private Veiculo veiculoSelecionado;

    public VeiculoCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new VeiculoController();
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

        Label lblTitulo = new Label("Cadastro de Veículos");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblModelo = new Label("Modelo:");
        txtModelo = new TextField();
        txtModelo.setPromptText("ex: Honda Civic");

        Label lblPlaca = new Label("Placa:");
        txtPlaca = new TextField();
        txtPlaca.setPromptText("ex: ABC1234");

        Label lblCor = new Label("Cor:");
        txtCor = new TextField();
        txtCor.setPromptText("ex: Preto, Prata");

        Label lblCapacidade = new Label("Capacidade:");
        txtCapacidade = new TextField();
        txtCapacidade.setPromptText("ex: 4");

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
                lblModelo, txtModelo,
                lblPlaca, txtPlaca,
                lblCor, txtCor,
                lblCapacidade, txtCapacidade,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Veiculo> criarTabela() {
        TableView<Veiculo> tabela = new TableView<>();

        TableColumn<Veiculo, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Veiculo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModelo()));

        TableColumn<Veiculo, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPlaca()));

        TableColumn<Veiculo, String> colCor = new TableColumn<>("Cor");
        colCor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCor()));

        TableColumn<Veiculo, Integer> colCapacidade = new TableColumn<>("Capacidade");
        colCapacidade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCapacidade()));

        tabela.getColumns().addAll(colId, colModelo, colPlaca, colCor, colCapacidade);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            veiculoSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (veiculoSelecionado != null) {
                txtModelo.setText(veiculoSelecionado.getModelo());
                txtPlaca.setText(veiculoSelecionado.getPlaca());
                txtCor.setText(veiculoSelecionado.getCor());
                txtCapacidade.setText(String.valueOf(veiculoSelecionado.getCapacidade()));
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtModelo.getText().isEmpty() || txtPlaca.getText().isEmpty() || txtCapacidade.getText().isEmpty()) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            int capacidade = Integer.parseInt(txtCapacidade.getText());
            if (capacidade <= 0) {
                DialogUtil.mostrarErro("Validação", "Capacidade deve ser maior que zero!");
                return;
            }

            controller.criar(
                    txtModelo.getText(),
                    txtPlaca.getText(),
                    txtCor.getText(),
                    capacidade
            );

            DialogUtil.mostrarInfo("Sucesso", "Veículo salvo com sucesso!");
            limpar();
            atualizarTabela();
        } catch (NumberFormatException e) {
            DialogUtil.mostrarErro("Erro", "Capacidade deve ser um número inteiro!");
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (veiculoSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um veículo para atualizar!");
                return;
            }

            int capacidade = Integer.parseInt(txtCapacidade.getText());
            controller.atualizar(
                    veiculoSelecionado.getId(),
                    txtModelo.getText(),
                    txtPlaca.getText(),
                    txtCor.getText(),
                    capacidade
            );

            DialogUtil.mostrarInfo("Sucesso", "Veículo atualizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (veiculoSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um veículo para excluir!");
                return;
            }

            controller.excluir(veiculoSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Veículo excluído com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtModelo.clear();
        txtPlaca.clear();
        txtCor.clear();
        txtCapacidade.clear();
        veiculoSelecionado = null;
    }

    private void atualizarTabela() {
        ObservableList<Veiculo> veiculos = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(veiculos);
    }
}

