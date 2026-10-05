package com.example.caronas.state;

import com.example.caronas.model.SolicitacaoCarona;

/** Estado concreto final: cancelada pelo passageiro ou pelo motorista. Nenhuma transição é permitida. */
public class EstadoCancelada implements EstadoSolicitacao {
    public static final String NOME = "cancelada";

    @Override
    public String nome() {
        return NOME;
    }

    @Override
    public void aceitar(SolicitacaoCarona c) {
        throw new TransicaoInvalidaException(NOME, "aceitar");
    }

    @Override
    public void recusar(SolicitacaoCarona c) {
        throw new TransicaoInvalidaException(NOME, "recusar");
    }

    @Override
    public void cancelar(SolicitacaoCarona c) {
        throw new TransicaoInvalidaException(NOME, "cancelar");
    }
}
