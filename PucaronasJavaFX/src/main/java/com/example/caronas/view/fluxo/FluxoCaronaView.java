package com.example.caronas.view.fluxo;

import com.example.caronas.facade.CaronasFacade;
import com.example.caronas.model.Carona;
import com.example.caronas.model.Notificacao;
import com.example.caronas.model.Pagamento;
import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.model.Usuario;
import com.example.caronas.service.CaronaService;
import com.example.caronas.service.NotificacaoService;
import com.example.caronas.service.PagamentoService;
import com.example.caronas.service.SolicitacaoCaronaService;
import com.example.caronas.service.UsuarioService;
import com.example.caronas.util.DadosDemonstracao;
import com.example.caronas.util.DialogUtil;
import com.example.caronas.view.BaseCrudView;
import com.example.caronas.view.MenuPrincipalView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Tela que demonstra o fluxo completo da carona usando APENAS o {@link CaronasFacade}.
 * Escolhe-se passageiro e carona em listas (sem copiar UUIDs) e acompanha-se o efeito de cada
 * ação: mudança de estado (State), preço (Strategy), vagas, notificações (Observer) e histórico.
 */
public class FluxoCaronaView extends BaseCrudView {
    private CaronasFacade facade;
    private UsuarioService usuarios;
    private CaronaService caronas;
    private SolicitacaoCaronaService solicitacoes;
    private PagamentoService pagamentos;
    private NotificacaoService notificacoes;

    private ComboBox<Usuario> cbPassageiro;
    private ComboBox<Carona> cbCarona;
    private ComboBox<String> cbMetodo;
    private Label lblPreco;
    private TableView<SolicitacaoCarona> tabela;
    private TextArea txtNotificacoes;

    public FluxoCaronaView(Stage stage, MenuPrincipalView menuView) {
        super(stage, menuView);
    }

    @Override
    protected void inicializarController() {
        facade = new CaronasFacade();
        usuarios = new UsuarioService();
        caronas = new CaronaService();
        solicitacoes = new SolicitacaoCaronaService();
        pagamentos = new PagamentoService();
        notificacoes = new NotificacaoService();
    }

    @Override
    protected void criarTela() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        Label titulo = new Label("Fluxo da Carona (Facade + State + Strategy + Observer)");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        cbPassageiro = new ComboBox<>();
        cbPassageiro.setPrefWidth(300);
        cbPassageiro.setPromptText("Passageiro");
        cbPassageiro.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Usuario u) { return u == null ? "" : u.getNome(); }
            @Override public Usuario fromString(String s) { return null; }
        });

        cbCarona = new ComboBox<>();
        cbCarona.setPrefWidth(520);
        cbCarona.setPromptText("Carona");
        cbCarona.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Carona c) {
                return c == null ? "" : c.getOrigem() + " → " + c.getDestino()
                        + "  | vagas: " + c.getVagasDisponiveis() + "  | política: " + c.getPoliticaPreco();
            }
            @Override public Carona fromString(String s) { return null; }
        });
        cbCarona.setOnAction(e -> atualizarPreco());

        lblPreco = new Label("Preço para o próximo passageiro: -");
        lblPreco.setStyle("-fx-font-weight: bold;");

        Button btnDemo = new Button("Carregar dados de demonstração");
        btnDemo.setOnAction(e -> carregarDemo());

        Label lblPassageiro = new Label("Passageiro:");
        Label lblCarona = new Label("Carona:");
        lblPassageiro.setMinWidth(Region.USE_PREF_SIZE);
        lblCarona.setMinWidth(Region.USE_PREF_SIZE);
        HBox linhaSelecao = new HBox(10, lblPassageiro, cbPassageiro, lblCarona, cbCarona);
        HBox linhaDemo = new HBox(15, lblPreco, btnDemo);

        Button btnSolicitar = new Button("1. Solicitar");
        Button btnAceitar = new Button("2. Aceitar (motorista)");
        Button btnRecusar = new Button("Recusar (motorista)");
        Button btnCancelar = new Button("Cancelar");
        cbMetodo = new ComboBox<>(FXCollections.observableArrayList("pix", "dinheiro"));
        cbMetodo.setValue("pix");
        Button btnPagar = new Button("3. Pagar");
        Button btnAtualizar = new Button("Atualizar");

        btnSolicitar.setOnAction(e -> executar(this::solicitar));
        btnAceitar.setOnAction(e -> executar(this::aceitar));
        btnRecusar.setOnAction(e -> executar(this::recusar));
        btnCancelar.setOnAction(e -> executar(this::cancelar));
        btnPagar.setOnAction(e -> executar(this::pagar));
        btnAtualizar.setOnAction(e -> recarregar());

        HBox linhaBotoes = new HBox(10, btnSolicitar, btnAceitar, btnRecusar, btnCancelar,
                new Label("Método:"), cbMetodo, btnPagar, btnAtualizar);

        tabela = criarTabela();
        tabela.setPrefHeight(230);

        txtNotificacoes = new TextArea();
        txtNotificacoes.setEditable(false);
        txtNotificacoes.setPrefRowCount(8);

        VBox conteudo = new VBox(10, titulo, linhaSelecao, linhaDemo, linhaBotoes, tabela,
                new Label("Notificações geradas pelos observadores:"), txtNotificacoes);
        root.setCenter(conteudo);
        root.setBottom(criarBotaoVoltar());

        scene = new Scene(root, 1000, 700);
        recarregar();
    }

    private TableView<SolicitacaoCarona> criarTabela() {
        TableView<SolicitacaoCarona> t = new TableView<>();

        TableColumn<SolicitacaoCarona, String> colPassageiro = new TableColumn<>("Passageiro");
        colPassageiro.setCellValueFactory(c -> new SimpleStringProperty(
                usuarios.buscarPorId(c.getValue().getPassageiroId()).map(Usuario::getNome).orElse("?")));

        TableColumn<SolicitacaoCarona, String> colRota = new TableColumn<>("Rota");
        colRota.setPrefWidth(280);
        colRota.setCellValueFactory(c -> new SimpleStringProperty(
                caronas.buscarPorId(c.getValue().getCaronaId())
                        .map(x -> x.getOrigem() + " → " + x.getDestino()).orElse("?")));

        TableColumn<SolicitacaoCarona, String> colStatus = new TableColumn<>("Estado (State)");
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        TableColumn<SolicitacaoCarona, String> colValor = new TableColumn<>("Pagamento");
        colValor.setPrefWidth(220);
        colValor.setCellValueFactory(c -> new SimpleStringProperty(
                pagamentos.buscarPorSolicitacao(c.getValue().getId()).stream().findFirst()
                        .map(p -> String.format(Locale.US, "R$ %.2f (%s)", p.getValor(), p.getStatus()))
                        .orElse("-")));

        t.getColumns().addAll(colPassageiro, colRota, colStatus, colValor);
        return t;
    }

    // ---------------- ações ----------------

    private void solicitar() throws Exception {
        Usuario u = cbPassageiro.getValue();
        Carona c = cbCarona.getValue();
        if (u == null || c == null) {
            throw new IllegalArgumentException("Escolha o passageiro e a carona");
        }
        facade.solicitarCarona(u.getId(), c.getId());
    }

    private void aceitar() throws Exception {
        Pagamento p = facade.aceitarSolicitacao(selecionada().getId());
        DialogUtil.mostrarInfo("Aceita", String.format(Locale.US, "Valor calculado pela política: R$ %.2f", p.getValor()));
    }

    private void recusar() throws Exception {
        facade.recusarSolicitacao(selecionada().getId());
    }

    private void cancelar() throws Exception {
        facade.cancelarSolicitacao(selecionada().getId());
    }

    private void pagar() throws Exception {
        SolicitacaoCarona s = selecionada();
        Pagamento p = pagamentos.buscarPorSolicitacao(s.getId()).stream()
                .filter(x -> "pendente".equals(x.getStatus())).findFirst()
                .orElseThrow(() -> new IllegalStateException("Não há pagamento pendente para esta solicitação"));
        facade.pagar(p.getId(), cbMetodo.getValue());
    }

    private SolicitacaoCarona selecionada() {
        SolicitacaoCarona s = tabela.getSelectionModel().getSelectedItem();
        if (s == null) {
            throw new IllegalArgumentException("Selecione uma solicitação na tabela");
        }
        return s;
    }

    @FunctionalInterface
    private interface Acao {
        void rodar() throws Exception;
    }

    private void executar(Acao acao) {
        try {
            acao.rodar();
        } catch (Exception e) {
            DialogUtil.mostrarErro("Não foi possível executar", e.getMessage());
        }
        recarregar();
    }

    private void carregarDemo() {
        try {
            boolean criou = DadosDemonstracao.popular();
            DialogUtil.mostrarInfo("Dados de demonstração",
                    criou ? "Usuários, motorista, veículo e 3 caronas (uma por política de preço) criados."
                            : "Os dados de demonstração já existem.");
        } catch (Exception e) {
            DialogUtil.mostrarErro("Erro", e.getMessage());
        }
        recarregar();
    }

    private void recarregar() {
        Usuario passageiroAtual = cbPassageiro.getValue();
        Carona caronaAtual = cbCarona.getValue();
        String idCarona = caronaAtual == null ? null : caronaAtual.getId();

        cbPassageiro.setItems(FXCollections.observableArrayList(usuarios.buscarPorTipo("passageiro")));
        cbCarona.setItems(FXCollections.observableArrayList(caronas.listar()));
        cbPassageiro.setValue(passageiroAtual);
        if (idCarona != null) {
            caronas.buscarPorId(idCarona).ifPresent(cbCarona::setValue);
        }

        tabela.setItems(FXCollections.observableArrayList(solicitacoes.listar()));
        tabela.refresh();

        txtNotificacoes.setText(notificacoes.listar().stream()
                .sorted(Comparator.comparing(Notificacao::getData))
                .map(n -> usuarios.buscarPorId(n.getUsuarioId()).map(Usuario::getNome).orElse("?")
                        + ": " + n.getMensagem())
                .collect(Collectors.joining("\n")));
        atualizarPreco();
    }

    private void atualizarPreco() {
        Carona c = cbCarona.getValue();
        if (c == null) {
            lblPreco.setText("Preço para o próximo passageiro: -");
            return;
        }
        try {
            lblPreco.setText(String.format(Locale.US, "Preço para o próximo passageiro: R$ %.2f (política %s)",
                    facade.simularPreco(c.getId()), c.getPoliticaPreco()));
        } catch (Exception e) {
            lblPreco.setText("Preço indisponível: " + e.getMessage());
        }
    }
}
