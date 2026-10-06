package com.example.caronas;

import com.example.caronas.event.*;
import com.example.caronas.facade.CaronasFacade;
import com.example.caronas.model.*;
import com.example.caronas.service.*;
import com.example.caronas.state.TransicaoInvalidaException;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/** Testes de integração do fluxo completo (Facade + State + Strategy + Observer + Singleton + Template Method). */
class FacadeFlowTest {
    private Path dir;
    private CaronasFacade facade;
    private CaronaService caronaService;
    private SolicitacaoCaronaService solicitacaoService;
    private PagamentoService pagamentoService;
    private NotificacaoService notificacaoService;
    private HistoricoCorrridaService historicoService;
    private String usuarioMotorista, motoristaId, passageiro1, passageiro2;

    @BeforeEach
    void setUp() throws Exception {
        dir = TestSupport.prepararDadosTemporarios();
        caronaService = new CaronaService();
        solicitacaoService = new SolicitacaoCaronaService();
        pagamentoService = new PagamentoService();
        notificacaoService = new NotificacaoService();
        historicoService = new HistoricoCorrridaService();
        MotoristaService motoristaService = new MotoristaService();
        UsuarioService usuarioService = new UsuarioService();

        BarramentoEventos bus = BarramentoEventos.getInstancia();
        bus.registrar(new NotificacaoObserver(notificacaoService, motoristaService));
        bus.registrar(new HistoricoObserver(historicoService));

        usuarioMotorista = usuarioService.criar("Carlos", "carlos@x.com", "1", "motorista").getId();
        passageiro1 = usuarioService.criar("Ana", "ana@x.com", "1", "passageiro").getId();
        passageiro2 = usuarioService.criar("Bia", "bia@x.com", "1", "passageiro").getId();
        motoristaId = motoristaService.criar(usuarioMotorista, "veiculo-1", "12345678900").getId();

        facade = new CaronasFacade(caronaService, solicitacaoService, pagamentoService, motoristaService, bus);
    }

    @AfterEach
    void tearDown() throws Exception {
        TestSupport.apagar(dir);
    }

    private Carona novaCarona(int vagas, String politica, double km, double base) throws Exception {
        return caronaService.criar(motoristaId, "PUCPR", "Centro", LocalDateTime.now().plusDays(1), vagas, km, base, politica);
    }

    @Test
    void solicitarCriaPendenteENotificaOMotorista() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 8.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());

        assertEquals("pendente", s.getStatus());
        var notifs = notificacaoService.buscarPorUsuario(usuarioMotorista);
        assertEquals(1, notifs.size());
        assertTrue(notifs.get(0).getMensagem().contains("Nova solicitação"));
    }

    @Test
    void aceitarReduzVagaGeraPagamentoPeloPrecoDaPoliticaENotificaPassageiro() throws Exception {
        Carona c = novaCarona(2, "POR_KM", 10, 1.5);   // R$ 15,00
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());

        Pagamento p = facade.aceitarSolicitacao(s.getId());

        assertEquals(15.0, p.getValor(), 0.001);
        assertEquals("pendente", p.getStatus());
        assertEquals("aceita", solicitacaoService.buscarPorId(s.getId()).orElseThrow().getStatus());
        assertEquals(1, caronaService.buscarPorId(c.getId()).orElseThrow().getVagasDisponiveis());
        var notifs = notificacaoService.buscarPorUsuario(passageiro1);
        assertEquals(1, notifs.size());
        assertTrue(notifs.get(0).getMensagem().contains("ACEITA"));
        assertTrue(notifs.get(0).getMensagem().contains("15.00"));
    }

    @Test
    void rateioFicaMaisBaratoConformeOCarroEncheOuSeja() throws Exception {
        Carona c = novaCarona(3, "RATEIO", 20, 1.0);   // custo total R$ 20
        SolicitacaoCarona s1 = facade.solicitarCarona(passageiro1, c.getId());
        SolicitacaoCarona s2 = facade.solicitarCarona(passageiro2, c.getId());

        assertEquals(10.0, facade.simularPreco(c.getId()), 0.001);
        Pagamento p1 = facade.aceitarSolicitacao(s1.getId());
        assertEquals(10.0, p1.getValor(), 0.001);
        assertEquals(6.67, facade.simularPreco(c.getId()), 0.001);
        Pagamento p2 = facade.aceitarSolicitacao(s2.getId());
        assertEquals(6.67, p2.getValor(), 0.001);
    }

    @Test
    void naoAceitaSemVagaENaoAlteraNada() throws Exception {
        Carona c = novaCarona(1, "FIXO", 10, 5.0);
        SolicitacaoCarona s1 = facade.solicitarCarona(passageiro1, c.getId());
        // segunda pessoa pede enquanto ainda há vaga...
        SolicitacaoCarona s2 = facade.solicitarCarona(passageiro2, c.getId());
        facade.aceitarSolicitacao(s1.getId());                       // ...a vaga acaba

        assertThrows(IllegalStateException.class, () -> facade.aceitarSolicitacao(s2.getId()));
        assertEquals("pendente", solicitacaoService.buscarPorId(s2.getId()).orElseThrow().getStatus());
        assertEquals(1, pagamentoService.listar().size());
        assertThrows(IllegalStateException.class, () -> facade.solicitarCarona(passageiro2, c.getId())
                .getId() /* sem vaga: nova solicitação também é barrada */);
    }

    @Test
    void naoPermiteSolicitacaoDuplicadaNemDoProprioMotorista() throws Exception {
        Carona c = novaCarona(3, "FIXO", 10, 5.0);
        facade.solicitarCarona(passageiro1, c.getId());
        assertThrows(IllegalStateException.class, () -> facade.solicitarCarona(passageiro1, c.getId()));
        assertThrows(IllegalStateException.class, () -> facade.solicitarCarona(usuarioMotorista, c.getId()));
    }

    @Test
    void recusadaNaoPodeSerAceitaDepois() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 5.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        facade.recusarSolicitacao(s.getId());

        assertThrows(TransicaoInvalidaException.class, () -> facade.aceitarSolicitacao(s.getId()));
        assertEquals(2, caronaService.buscarPorId(c.getId()).orElseThrow().getVagasDisponiveis());
        assertTrue(pagamentoService.listar().isEmpty());
        assertTrue(notificacaoService.buscarPorUsuario(passageiro1).get(0).getMensagem().contains("RECUSADA"));
    }

    @Test
    void pagarGeraHistoricoENotificaMotorista() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 9.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        Pagamento p = facade.aceitarSolicitacao(s.getId());

        Pagamento pago = facade.pagar(p.getId(), "dinheiro");

        assertEquals("pago", pago.getStatus());
        assertEquals("dinheiro", pago.getMetodo());
        assertNotNull(pago.getDataPagamento());
        var hist = historicoService.listar();
        assertEquals(1, hist.size());
        assertEquals(9.0, hist.get(0).getValor(), 0.001);
        assertEquals(passageiro1, hist.get(0).getPassageiroId());
        assertEquals("PUCPR → Centro", hist.get(0).getTrajeto());
        assertTrue(notificacaoService.buscarPorUsuario(usuarioMotorista).stream()
                .anyMatch(n -> n.getMensagem().contains("Pagamento")));
    }

    @Test
    void naoPagaDuasVezesNemSolicitacaoNaoAceita() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 9.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        Pagamento p = facade.aceitarSolicitacao(s.getId());
        facade.pagar(p.getId(), "pix");
        assertThrows(IllegalStateException.class, () -> facade.pagar(p.getId(), "pix"));
        assertEquals(1, historicoService.listar().size());

        assertThrows(IllegalArgumentException.class, () -> facade.pagar(p.getId(), "cartao"));
    }

    @Test
    void cancelarSolicitacaoAceitaDevolveVagaECancelaPagamentoPendente() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 9.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        Pagamento p = facade.aceitarSolicitacao(s.getId());
        assertEquals(1, caronaService.buscarPorId(c.getId()).orElseThrow().getVagasDisponiveis());

        facade.cancelarSolicitacao(s.getId());

        assertEquals("cancelada", solicitacaoService.buscarPorId(s.getId()).orElseThrow().getStatus());
        assertEquals(2, caronaService.buscarPorId(c.getId()).orElseThrow().getVagasDisponiveis());
        assertEquals("cancelado", pagamentoService.buscarPorId(p.getId()).orElseThrow().getStatus());
        assertThrows(IllegalStateException.class, () -> facade.pagar(p.getId(), "pix"));
    }

    @Test
    void naoCancelaSolicitacaoJaPaga() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 9.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        Pagamento p = facade.aceitarSolicitacao(s.getId());
        facade.pagar(p.getId(), "pix");

        assertThrows(IllegalStateException.class, () -> facade.cancelarSolicitacao(s.getId()));
        assertEquals("aceita", solicitacaoService.buscarPorId(s.getId()).orElseThrow().getStatus());
    }

    @Test
    void crudDeAtualizacaoRespeitaMaquinaDeEstados() throws Exception {
        Carona c = novaCarona(2, "FIXO", 10, 9.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        facade.recusarSolicitacao(s.getId());

        assertThrows(TransicaoInvalidaException.class,
                () -> solicitacaoService.atualizar(s.getId(), passageiro1, c.getId(), "aceita"));
        assertThrows(TransicaoInvalidaException.class,
                () -> solicitacaoService.atualizar(s.getId(), passageiro1, c.getId(), "pendente"));
    }

    @Test
    void novoObservadorFuncionaSemAlterarOFacade() throws Exception {
        int[] contador = {0};
        BarramentoEventos.getInstancia().registrar(e -> contador[0]++);
        Carona c = novaCarona(2, "FIXO", 10, 9.0);
        SolicitacaoCarona s = facade.solicitarCarona(passageiro1, c.getId());
        facade.aceitarSolicitacao(s.getId());
        assertEquals(2, contador[0]);   // SOLICITACAO_CRIADA + SOLICITACAO_ACEITA
    }
}
