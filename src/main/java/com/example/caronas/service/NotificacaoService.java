package com.example.caronas.service;

import com.example.caronas.model.Notificacao;
import com.example.caronas.repository.NotificacaoRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotificacaoService {
    private NotificacaoRepository repository;

    public NotificacaoService() {
        this.repository = new NotificacaoRepository();
    }

    public Notificacao criar(String usuarioId, String mensagem) throws IOException {
        if (usuarioId == null || usuarioId.isEmpty() || mensagem == null || mensagem.isEmpty()) {
            throw new IllegalArgumentException("Usuário e mensagem são obrigatórios");
        }

        Notificacao notificacao = new Notificacao(
                UUID.randomUUID().toString(),
                usuarioId,
                mensagem,
                LocalDateTime.now(),
                false
        );
        repository.inserir(notificacao);
        return notificacao;
    }

    public List<Notificacao> listar() {
        return repository.listar();
    }

    public Optional<Notificacao> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<Notificacao> buscarPorUsuario(String usuarioId) {
        return repository.buscarPorUsuario(usuarioId);
    }

    public List<Notificacao> buscarNaoLidas(String usuarioId) {
        return repository.buscarNaoLidas(usuarioId);
    }

    public int contarNaoLidas(String usuarioId) {
        return repository.contarNaoLidas(usuarioId);
    }

    public Notificacao marcarComoLida(String id) throws IOException {
        Optional<Notificacao> notificacao = repository.buscarPorId(id);
        if (notificacao.isEmpty()) {
            throw new IllegalArgumentException("Notificação não encontrada");
        }
        notificacao.get().setLida(true);
        repository.atualizar(notificacao.get());
        return notificacao.get();
    }

    public Notificacao atualizar(String id, String usuarioId, String mensagem, boolean lida) throws IOException {
        Optional<Notificacao> notificacao = repository.buscarPorId(id);
        if (notificacao.isEmpty()) {
            throw new IllegalArgumentException("Notificação não encontrada");
        }
        notificacao.get().setUsuarioId(usuarioId);
        notificacao.get().setMensagem(mensagem);
        notificacao.get().setLida(lida);
        repository.atualizar(notificacao.get());
        return notificacao.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Notificacao> notificacao = repository.buscarPorId(id);
        if (notificacao.isEmpty()) {
            throw new IllegalArgumentException("Notificação não encontrada");
        }
        repository.excluirPorId(id);
    }
}

