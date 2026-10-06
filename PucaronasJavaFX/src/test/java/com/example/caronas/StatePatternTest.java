package com.example.caronas;

import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.state.TransicaoInvalidaException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StatePatternTest {

    private SolicitacaoCarona nova() {
        return new SolicitacaoCarona("s1", "p1", "c1", "pendente", LocalDateTime.now());
    }

    @Test
    void pendentePodeSerAceitaRecusadaOuCancelada() {
        SolicitacaoCarona a = nova(); a.aceitar();   assertEquals("aceita", a.getStatus());
        SolicitacaoCarona r = nova(); r.recusar();   assertEquals("recusada", r.getStatus());
        SolicitacaoCarona c = nova(); c.cancelar();  assertEquals("cancelada", c.getStatus());
    }

    @Test
    void aceitaSoPodeSerCancelada() {
        SolicitacaoCarona s = nova(); s.aceitar();
        assertThrows(TransicaoInvalidaException.class, s::aceitar);
        assertThrows(TransicaoInvalidaException.class, s::recusar);
        s.cancelar();
        assertEquals("cancelada", s.getStatus());
    }

    @Test
    void estadosFinaisNaoPermitemNenhumaTransicao() {
        SolicitacaoCarona r = nova(); r.recusar();
        assertThrows(TransicaoInvalidaException.class, r::aceitar);
        assertThrows(TransicaoInvalidaException.class, r::cancelar);

        SolicitacaoCarona c = nova(); c.cancelar();
        assertThrows(TransicaoInvalidaException.class, c::aceitar);
        assertThrows(TransicaoInvalidaException.class, c::recusar);
    }

    @Test
    void falhaNaTransicaoNaoAlteraOStatus() {
        SolicitacaoCarona r = nova(); r.recusar();
        assertThrows(TransicaoInvalidaException.class, r::aceitar);
        assertEquals("recusada", r.getStatus());
    }

    @Test
    void statusDesconhecidoELancadoComoErro() {
        SolicitacaoCarona s = new SolicitacaoCarona("s1", "p1", "c1", "xyz", LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, s::aceitar);
    }
}
