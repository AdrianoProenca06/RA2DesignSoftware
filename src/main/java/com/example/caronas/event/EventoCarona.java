package com.example.caronas.event;
public record EventoCarona(Tipo tipo, String usuarioId, String mensagem) {
    public enum Tipo { SOLICITACAO_CRIADA, SOLICITACAO_ACEITA, SOLICITACAO_RECUSADA, SOLICITACAO_CANCELADA, PAGAMENTO_REALIZADO }
}
