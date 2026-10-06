package com.example.caronas.event;

import com.example.caronas.model.Carona;
import com.example.caronas.model.Pagamento;
import com.example.caronas.model.SolicitacaoCarona;

import java.time.LocalDateTime;

/** Mensagem imutável publicada no barramento quando algo relevante acontece no domínio. */
public record EventoCarona(TipoEvento tipo,
                           SolicitacaoCarona solicitacao,
                           Carona carona,
                           Pagamento pagamento,
                           LocalDateTime quando) {

    public static EventoCarona de(TipoEvento tipo, SolicitacaoCarona solicitacao, Carona carona, Pagamento pagamento) {
        return new EventoCarona(tipo, solicitacao, carona, pagamento, LocalDateTime.now());
    }
}
