package com.example.caronas.controller;

import com.example.caronas.model.Notificacao;
import com.example.caronas.service.NotificacaoService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class NotificacaoController {
    private NotificacaoService service;

    public NotificacaoController() {
        this.service = new NotificacaoService();
    }

    public Notificacao criar(String usuarioId, String mensagem) throws IOException {
        return service.criar(usuarioId, mensagem);
    }

    public List<Notificacao> listar() {
        return service.listar();
    }

    public Optional<Notificacao> buscarPorId(String id) {
        return service.buscarPorId(id);
    }

    public List<Notificacao> buscarPorUsuario(String usuarioId) {
        return service.buscarPorUsuario(usuarioId);
    }

    public List<Notificacao> buscarNaoLidas(String usuarioId) {
        return service.buscarNaoLidas(usuarioId);
    }

    public int contarNaoLidas(String usuarioId) {
        return service.contarNaoLidas(usuarioId);
    }

    public Notificacao marcarComoLida(String id) throws IOException {
        return service.marcarComoLida(id);
    }

    public Notificacao atualizar(String id, String usuarioId, String mensagem, boolean lida) throws IOException {
        return service.atualizar(id, usuarioId, mensagem, lida);
    }

    public void excluir(String id) throws IOException {
        service.excluir(id);
    }
}

