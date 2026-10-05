package com.example.caronas.event;

/**
 * Observer concreto mínimo: registra no console cada evento. Existe para demonstrar que novos
 * consumidores podem ser acrescentados sem alterar o Facade nem os serviços (Aberto/Fechado).
 */
public class AuditoriaObserver implements ObservadorEventos {
    @Override
    public void atualizar(EventoCarona e) {
        System.out.println("[AUDITORIA " + e.quando() + "] " + e.tipo()
                + (e.solicitacao() != null ? " solicitacao=" + e.solicitacao().getId() : ""));
    }
}
