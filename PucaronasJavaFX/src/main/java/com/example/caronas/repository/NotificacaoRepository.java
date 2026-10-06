package com.example.caronas.repository;

import com.example.caronas.model.Notificacao;
import java.util.List;
import java.util.Optional;

public class NotificacaoRepository extends BaseRepository<Notificacao> {

    public NotificacaoRepository() {
        super("notificacoes");
    }

    @Override
    protected String idDe(Notificacao entidade) {
        return entidade.getId();
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

}
