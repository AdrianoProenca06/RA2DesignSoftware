package com.example.caronas.state;

import com.example.caronas.model.SolicitacaoCarona;

/** Estado concreto: o motorista confirmou. Só pode ser cancelada (libera a vaga). */
public class EstadoAceita implements EstadoSolicitacao {
    public static final String NOME = "aceita";

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
        c.mudarPara(EstadosSolicitacao.CANCELADA);
    }
}
