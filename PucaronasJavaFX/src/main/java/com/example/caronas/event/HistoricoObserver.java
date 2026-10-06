package com.example.caronas.event;

import com.example.caronas.service.HistoricoCorrridaService;

import java.io.IOException;

/** Observer concreto: quando um pagamento é concluído, registra a corrida no histórico. */
public class HistoricoObserver implements ObservadorEventos {
    private final HistoricoCorrridaService historico;

    public HistoricoObserver(HistoricoCorrridaService historico) {
        this.historico = historico;
    }

    @Override
    public void atualizar(EventoCarona e) {
        if (e.tipo() != TipoEvento.PAGAMENTO_REALIZADO) {
            return;
        }
        try {
            historico.criar(
                    e.carona().getMotoristaId(),
                    e.solicitacao().getPassageiroId(),
                    e.carona().getOrigem() + " → " + e.carona().getDestino(),
                    e.pagamento().getValor());
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao gravar histórico", ex);
        }
    }
}
