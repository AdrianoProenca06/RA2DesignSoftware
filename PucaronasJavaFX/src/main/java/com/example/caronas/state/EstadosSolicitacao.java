package com.example.caronas.state;

/**
 * Instâncias compartilhadas dos estados (eles não guardam dados, então uma única instância
 * de cada serve a todas as solicitações) e conversão do nome persistido para o objeto de estado.
 */
public final class EstadosSolicitacao {
    public static final EstadoSolicitacao PENDENTE = new EstadoPendente();
    public static final EstadoSolicitacao ACEITA = new EstadoAceita();
    public static final EstadoSolicitacao RECUSADA = new EstadoRecusada();
    public static final EstadoSolicitacao CANCELADA = new EstadoCancelada();

    private EstadosSolicitacao() {
    }

    public static EstadoSolicitacao porNome(String nome) {
        if (nome == null) {
            return PENDENTE;
        }
        return switch (nome) {
            case EstadoPendente.NOME -> PENDENTE;
            case EstadoAceita.NOME -> ACEITA;
            case EstadoRecusada.NOME -> RECUSADA;
            case EstadoCancelada.NOME -> CANCELADA;
            default -> throw new IllegalArgumentException("Status de solicitação desconhecido: " + nome);
        };
    }
}
