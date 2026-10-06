package com.example.caronas.repository;

import com.example.caronas.model.Avaliacao;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AvaliacaoRepository extends BaseRepository<Avaliacao> {
    
    public AvaliacaoRepository() {
        super("avaliacoes");
    }

    public Optional<Avaliacao> buscarPorId(String id) {
        List<Avaliacao> avaliacoes = listar();
        return avaliacoes.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
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

    @Override
    public void atualizar(Avaliacao avaliacao) throws IOException {
        List<Avaliacao> avaliacoes = listar();
        for (int i = 0; i < avaliacoes.size(); i++) {
            if (avaliacoes.get(i).getId().equals(avaliacao.getId())) {
                avaliacoes.set(i, avaliacao);
                break;
            }
        }
        salvarLista(avaliacoes);
    }

    @Override
    public void excluir(Avaliacao avaliacao) throws IOException {
        List<Avaliacao> avaliacoes = listar();
        avaliacoes.removeIf(a -> a.getId().equals(avaliacao.getId()));
        salvarLista(avaliacoes);
    }

    public void excluirPorId(String id) throws IOException {
        List<Avaliacao> avaliacoes = listar();
        avaliacoes.removeIf(a -> a.getId().equals(id));
        salvarLista(avaliacoes);
    }
}

