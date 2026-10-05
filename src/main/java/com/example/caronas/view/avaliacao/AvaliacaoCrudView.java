package com.example.caronas.view.avaliacao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.AvaliacaoController;
import com.example.caronas.model.Avaliacao;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class AvaliacaoCrudView extends BaseCrudView {
    private AvaliacaoController controller;
    private TableView<Avaliacao> tabela;
    private TextField txtAvaliadorId;
    private TextField txtUsuarioAvaliadoId;
    private ComboBox<Integer> cbNota;
    private TextArea txtComentario;
    private Avaliacao avaliacaoSelecionada;

    public AvaliacaoCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new AvaliacaoController();
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

        Label lblTitulo = new Label("Gerenciar Avaliações");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblAvaliadorId = new Label("ID Avaliador:");
        txtAvaliadorId = new TextField();
        txtAvaliadorId.setPromptText("ID de quem está avaliando");

        Label lblUsuarioAvaliadoId = new Label("ID Usuário Avaliado:");
        txtUsuarioAvaliadoId = new TextField();
        txtUsuarioAvaliadoId.setPromptText("ID do usuário avaliado");
        txtUsuarioAvaliadoId.setEditable(true);

        Label lblNota = new Label("Nota (1-5):");
        cbNota = new ComboBox<>();
        cbNota.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        cbNota.setPrefWidth(200);

        Label lblComentario = new Label("Comentário:");
        txtComentario = new TextArea();
        txtComentario.setPrefHeight(80);
        txtComentario.setWrapText(true);

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
                lblAvaliadorId, txtAvaliadorId,
                lblUsuarioAvaliadoId, txtUsuarioAvaliadoId,
                lblNota, cbNota,
                lblComentario, txtComentario,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Avaliacao> criarTabela() {
        TableView<Avaliacao> tabela = new TableView<>();

        TableColumn<Avaliacao, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Avaliacao, String> colAvaliadorId = new TableColumn<>("Avaliador ID");
        colAvaliadorId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAvaliadorId()));

        TableColumn<Avaliacao, String> colUsuarioAvaliadoId = new TableColumn<>("Usuário Avaliado ID");
        colUsuarioAvaliadoId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuarioAvaliadoId()));

        TableColumn<Avaliacao, Integer> colNota = new TableColumn<>("Nota");
        colNota.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getNota()));

        TableColumn<Avaliacao, String> colComentario = new TableColumn<>("Comentário");
        colComentario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getComentario()));

        TableColumn<Avaliacao, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getData().toString()));

        tabela.getColumns().addAll(colId, colAvaliadorId, colUsuarioAvaliadoId, colNota, colComentario, colData);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            avaliacaoSelecionada = tabela.getSelectionModel().getSelectedItem();
            if (avaliacaoSelecionada != null) {
                txtAvaliadorId.setText(avaliacaoSelecionada.getAvaliadorId());
                txtUsuarioAvaliadoId.setText(avaliacaoSelecionada.getUsuarioAvaliadoId());
                cbNota.setValue(avaliacaoSelecionada.getNota());
                txtComentario.setText(avaliacaoSelecionada.getComentario());
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtAvaliadorId.getText().isEmpty() || txtUsuarioAvaliadoId.getText().isEmpty() || cbNota.getValue() == null) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.criar(
                    txtAvaliadorId.getText(),
                    txtUsuarioAvaliadoId.getText(),
                    cbNota.getValue(),
                    txtComentario.getText()
            );

            DialogUtil.mostrarInfo("Sucesso", "Avaliação salva com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (avaliacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma avaliação para atualizar!");
                return;
            }

            controller.atualizar(
                    avaliacaoSelecionada.getId(),
                    txtAvaliadorId.getText(),
                    txtUsuarioAvaliadoId.getText(),
                    cbNota.getValue(),
                    txtComentario.getText()
            );

            DialogUtil.mostrarInfo("Sucesso", "Avaliação atualizada com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (avaliacaoSelecionada == null) {
                DialogUtil.mostrarErro("Validação", "Selecione uma avaliação para excluir!");
                return;
            }

            controller.excluir(avaliacaoSelecionada.getId());
            DialogUtil.mostrarInfo("Sucesso", "Avaliação excluída!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtAvaliadorId.clear();
        txtUsuarioAvaliadoId.clear();
        cbNota.setValue(null);
        txtComentario.clear();
        avaliacaoSelecionada = null;
    }

    private void atualizarTabela() {
        ObservableList<Avaliacao> avaliacoes = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(avaliacoes);
    }
}

