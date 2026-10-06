package com.example.caronas.view.usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.caronas.controller.UsuarioController;
import com.example.caronas.model.Usuario;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;

public class UsuarioCrudView extends BaseCrudView {
    private UsuarioController controller;
    private TableView<Usuario> tabela;
    private TextField txtNome;
    private TextField txtEmail;
    private TextField txtSenha;
    private ComboBox<String> cbTipo;
    private Usuario usuarioSelecionado;

    public UsuarioCrudView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        this.controller = new UsuarioController();
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

        Label lblTitulo = new Label("Cadastro de Usuários");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label lblNome = new Label("Nome:");
        txtNome = new TextField();
        txtNome.setPromptText("Digite o nome completo");

        Label lblEmail = new Label("Email:");
        txtEmail = new TextField();
        txtEmail.setPromptText("Digite o email");

        Label lblSenha = new Label("Senha:");
        txtSenha = new TextField();
        txtSenha.setPromptText("Digite a senha");

        Label lblTipo = new Label("Tipo:");
        cbTipo = new ComboBox<>();
        cbTipo.setItems(FXCollections.observableArrayList("motorista", "passageiro"));
        cbTipo.setPrefWidth(200);

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
                lblNome, txtNome,
                lblEmail, txtEmail,
                lblSenha, txtSenha,
                lblTipo, cbTipo,
                hboxBotoes
        );

        return vbox;
    }

    private TableView<Usuario> criarTabela() {
        TableView<Usuario> tabela = new TableView<>();

        TableColumn<Usuario, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Usuario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));

        TableColumn<Usuario, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Usuario, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo()));

        tabela.getColumns().addAll(colId, colNome, colEmail, colTipo);
        tabela.setPrefHeight(300);

        tabela.setOnMouseClicked(e -> {
            usuarioSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (usuarioSelecionado != null) {
                txtNome.setText(usuarioSelecionado.getNome());
                txtEmail.setText(usuarioSelecionado.getEmail());
                txtSenha.setText(usuarioSelecionado.getSenha());
                cbTipo.setValue(usuarioSelecionado.getTipo());
            }
        });

        return tabela;
    }

    private void salvar() {
        try {
            if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty() || cbTipo.getValue() == null) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.criar(
                    txtNome.getText(),
                    txtEmail.getText(),
                    txtSenha.getText(),
                    cbTipo.getValue()
            );

            DialogUtil.mostrarInfo("Sucesso", "Usuário salvo com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void atualizar() {
        try {
            if (usuarioSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um usuário para atualizar!");
                return;
            }

            if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty() || cbTipo.getValue() == null) {
                DialogUtil.mostrarErro("Validação", "Preencha todos os campos obrigatórios!");
                return;
            }

            controller.atualizar(
                    usuarioSelecionado.getId(),
                    txtNome.getText(),
                    txtEmail.getText(),
                    txtSenha.getText(),
                    cbTipo.getValue()
            );

            DialogUtil.mostrarInfo("Sucesso", "Usuário atualizado com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void excluir() {
        try {
            if (usuarioSelecionado == null) {
                DialogUtil.mostrarErro("Validação", "Selecione um usuário para excluir!");
                return;
            }

            controller.excluir(usuarioSelecionado.getId());
            DialogUtil.mostrarInfo("Sucesso", "Usuário excluído com sucesso!");
            limpar();
            atualizarTabela();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
    }

    private void limpar() {
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        cbTipo.setValue(null);
        usuarioSelecionado = null;
    }

    private void atualizarTabela() {
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(controller.listar());
        tabela.setItems(usuarios);
    }
}

