package com.example.caronas.event;
import com.example.caronas.service.NotificacaoService;
public class NotificacaoObserver implements EventoObserver {
    private final NotificacaoService service;
    public NotificacaoObserver(NotificacaoService service) { this.service = service; }
    public void atualizar(EventoCarona evento) {
        try { service.criar(evento.usuarioId(), evento.mensagem()); }
        catch (Exception e) { throw new IllegalStateException("Falha ao registrar notificação", e); }
    }
}
