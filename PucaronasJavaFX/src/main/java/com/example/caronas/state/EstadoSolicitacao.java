package com.example.caronas.state;

import com.example.caronas.model.SolicitacaoCarona;

/**
 * PADRÃO: State (GoF - comportamental) – papel de "State".
 *
 * <p>Cada estado do ciclo de vida de uma {@link SolicitacaoCarona} é uma classe que sabe
 * quais transições são válidas a partir dele. Elimina os {@code if (status.equals(...))}
 * espalhados e impede transições absurdas (ex.: aceitar uma solicitação já recusada).
 *
 * <pre>
 *   PENDENTE ──aceitar──▶ ACEITA ──cancelar──▶ CANCELADA
 *      │ └─recusar──▶ RECUSADA
 *      └────cancelar──▶ CANCELADA
 * </pre>
 */
public interface EstadoSolicitacao {

    /** Nome persistido em {@code SolicitacaoCarona.status}. */
    String nome();

    void aceitar(SolicitacaoCarona contexto);

    void recusar(SolicitacaoCarona contexto);

    void cancelar(SolicitacaoCarona contexto);
}
