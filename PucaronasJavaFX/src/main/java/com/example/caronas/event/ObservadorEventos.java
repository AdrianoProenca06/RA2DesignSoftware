package com.example.caronas.event;

/**
 * PADRÃO: Observer (GoF - comportamental) – papel de "Observer".
 * Quem quiser reagir a eventos do domínio implementa esta interface e se registra no
 * {@link BarramentoEventos}. O código que publica o evento não conhece quem o consome.
 */
public interface ObservadorEventos {
    void atualizar(EventoCarona evento);
}
