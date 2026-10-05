package com.example.caronas;

import com.example.caronas.event.*;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverSingletonTest {
    private Path dir;

    @BeforeEach
    void setUp() throws Exception {
        dir = TestSupport.prepararDadosTemporarios();
    }

    @AfterEach
    void tearDown() throws Exception {
        TestSupport.apagar(dir);
    }

    @Test
    void barramentoEUmSingleton() {
        assertSame(BarramentoEventos.getInstancia(), BarramentoEventos.getInstancia());
    }

    @Test
    void observadoresRegistradosRecebemOEvento() {
        List<TipoEvento> recebidos = new ArrayList<>();
        BarramentoEventos.getInstancia().registrar(e -> recebidos.add(e.tipo()));
        BarramentoEventos.getInstancia().publicar(EventoCarona.de(TipoEvento.SOLICITACAO_CRIADA, null, null, null));
        assertEquals(List.of(TipoEvento.SOLICITACAO_CRIADA), recebidos);
    }

    @Test
    void registrarOMesmoObservadorDuasVezesNaoDuplicaNotificacao() {
        int[] n = {0};
        ObservadorEventos o = e -> n[0]++;
        BarramentoEventos b = BarramentoEventos.getInstancia();
        b.registrar(o);
        b.registrar(o);
        b.publicar(EventoCarona.de(TipoEvento.PAGAMENTO_REALIZADO, null, null, null));
        assertEquals(1, n[0]);
    }

    @Test
    void observadorRemovidoNaoRecebeMais() {
        int[] n = {0};
        ObservadorEventos o = e -> n[0]++;
        BarramentoEventos b = BarramentoEventos.getInstancia();
        b.registrar(o);
        b.remover(o);
        b.publicar(EventoCarona.de(TipoEvento.PAGAMENTO_REALIZADO, null, null, null));
        assertEquals(0, n[0]);
    }

    @Test
    void falhaDeUmObservadorNaoImpedeOsDemais() {
        int[] n = {0};
        BarramentoEventos b = BarramentoEventos.getInstancia();
        b.registrar(e -> { throw new IllegalStateException("boom"); });
        b.registrar(e -> n[0]++);
        assertDoesNotThrow(() -> b.publicar(EventoCarona.de(TipoEvento.SOLICITACAO_ACEITA, null, null, null)));
        assertEquals(1, n[0]);
    }
}
