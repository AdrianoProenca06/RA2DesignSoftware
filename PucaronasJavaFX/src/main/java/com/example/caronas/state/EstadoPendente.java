package com.example.caronas.state;

import com.example.caronas.model.SolicitacaoCarona;

/** Estado concreto inicial: aguardando decisão do motorista. Aceita todas as transições. */
public class EstadoPendente implements EstadoSolicitacao {
    public static final String NOME = "pendente";

    @Override
    public String nome() {
        return NOME;
    }

    @Override
    public void aceitar(SolicitacaoCarona c) {
        c.mudarPara(EstadosSolicitacao.ACEITA);
    }

    @Override
    public void recusar(SolicitacaoCarona c) {
        c.mudarPara(EstadosSolicitacao.RECUSADA);
    }

    @Override
    public void cancelar(SolicitacaoCarona c) {
        c.mudarPara(EstadosSolicitacao.CANCELADA);
    }
}
