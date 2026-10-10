package com.example.caronas;

import com.example.caronas.event.EventBus;
import com.example.caronas.event.EventoCarona;
import com.example.caronas.event.EventoObserver;
import java.util.concurrent.atomic.AtomicInteger;
import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.strategy.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PatternTests {
    @Test void strategyCalculaTresPoliticas() {
        assertEquals(20.0, new PrecoFixoStrategy().calcular(20, 10, 0), 0.001);
        assertEquals(25.0, new PrecoPorKmStrategy().calcular(2.5, 10, 0), 0.001);
        assertEquals(10.0, new PrecoRateioStrategy().calcular(30, 10, 1), 0.001);
    }
    @Test void stateRestringeTransicoes() {
        SolicitacaoCarona s = new SolicitacaoCarona("s","p","c","pendente", LocalDateTime.now());
        s.aceitar(); assertEquals("aceita", s.getStatus());
        assertThrows(IllegalStateException.class, () -> s.recusar());
        s.cancelar(); assertEquals("cancelada", s.getStatus());
        assertThrows(IllegalStateException.class, () -> s.aceitar());
    }
    @Test void observerRecebeEventoUmaVez() {
        EventBus bus = EventBus.getInstance();
        AtomicInteger recebidos = new AtomicInteger();
        EventoObserver observador = evento -> recebidos.incrementAndGet();
        bus.registrar(observador);
        try {
            bus.publicar(new EventoCarona(EventoCarona.Tipo.SOLICITACAO_CRIADA, "usuario", "teste"));
            assertEquals(1, recebidos.get());
        } finally {
            bus.remover(observador);
        }
    }
    @Test void singletonMantemMesmaInstancia() {
        assertSame(EventBus.getInstance(), EventBus.getInstance());
    }
}
