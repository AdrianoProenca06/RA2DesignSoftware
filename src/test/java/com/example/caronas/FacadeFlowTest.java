package com.example.caronas;

import com.example.caronas.facade.CaronasFacade;
import com.example.caronas.model.Carona;
import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.repository.CaronaRepository;
import com.example.caronas.repository.SolicitacaoCaronaRepository;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class FacadeFlowTest {
    @Test void naoPermiteSolicitarCaronaInexistente() {
        CaronasFacade facade = new CaronasFacade();
        assertThrows(IllegalArgumentException.class,
            () -> facade.solicitar("inexistente-" + UUID.randomUUID(), "passageiro"));
    }
    @Test void naoPermiteAceitarSolicitacaoInexistente() {
        CaronasFacade facade = new CaronasFacade();
        assertThrows(IllegalArgumentException.class,
            () -> facade.aceitar("inexistente-" + UUID.randomUUID(), "pix"));
    }
}
