package com.example.caronas.repository;

import com.example.caronas.model.Notificacao;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class NotificacaoRepository extends BaseRepository<Notificacao> {

    public NotificacaoRepository() {
        super("notificacoes");
    }

    public Optional<Notificacao> buscarPorId(String id) {
        List<Notificacao> notificacoes = listar();
        return notificacoes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    public List<Notificacao> buscarPorUsuario(String usuarioId) {
        List<Notificacao> notificacoes = listar();
        return notificacoes.stream()
                .filter(n -> n.getUsuarioId().equals(usuarioId))
                .toList();
    }

    public List<Notificacao> buscarNaoLidas(String usuarioId) {
        List<Notificacao> notificacoes = listar();
        return notificacoes.stream()
                .filter(n -> n.getUsuarioId().equals(usuarioId) && !n.isLida())
                .toList();
    }

    public int contarNaoLidas(String usuarioId) {
        return (int) buscarNaoLidas(usuarioId).size();
    }

    @Override
    public void atualizar(Notificacao notificacao) throws IOException {
        List<Notificacao> notificacoes = listar();
        for (int i = 0; i < notificacoes.size(); i++) {
            if (notificacoes.get(i).getId().equals(notificacao.getId())) {
                notificacoes.set(i, notificacao);
                break;
            }
        }
        salvarLista(notificacoes);
    }

    @Override
    public void excluir(Notificacao notificacao) throws IOException {
        List<Notificacao> notificacoes = listar();
        notificacoes.removeIf(n -> n.getId().equals(notificacao.getId()));
        salvarLista(notificacoes);
    }

    public void excluirPorId(String id) throws IOException {
        List<Notificacao> notificacoes = listar();
        notificacoes.removeIf(n -> n.getId().equals(id));
        salvarLista(notificacoes);
    }
}

