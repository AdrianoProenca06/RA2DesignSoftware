package com.example.caronas.state;

/** Lançada quando uma operação não é permitida no estado atual da solicitação. */
public class TransicaoInvalidaException extends IllegalStateException {
    public TransicaoInvalidaException(String estadoAtual, String operacao) {
        super("Não é possível " + operacao + " uma solicitação que está '" + estadoAtual + "'.");
    }
}
