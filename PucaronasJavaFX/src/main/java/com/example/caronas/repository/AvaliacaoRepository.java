package com.example.caronas.repository;

import com.example.caronas.model.Avaliacao;
import java.util.List;
import java.util.Optional;

public class AvaliacaoRepository extends BaseRepository<Avaliacao> {
    
    public AvaliacaoRepository() {
        super("avaliacoes");
    }

    @Override
    protected String idDe(Avaliacao entidade) {
        return entidade.getId();
    }

    public List<Avaliacao> buscarPorUsuarioAvaliado(String usuarioAvaliadoId) {
        List<Avaliacao> avaliacoes = listar();
        return avaliacoes.stream()
                .filter(a -> a.getUsuarioAvaliadoId().equals(usuarioAvaliadoId))
                .toList();
    }

    public List<Avaliacao> buscarPorAvaliador(String avaliadorId) {
        List<Avaliacao> avaliacoes = listar();
        return avaliacoes.stream()
                .filter(a -> a.getAvaliadorId().equals(avaliadorId))
                .toList();
    }

    public double calcularMediaAvaliacoes(String usuarioAvaliadoId) {
        List<Avaliacao> avaliacoes = buscarPorUsuarioAvaliado(usuarioAvaliadoId);
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        return avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);
    }

}
