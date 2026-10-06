package com.example.caronas.service;

import com.example.caronas.model.Avaliacao;
import com.example.caronas.repository.AvaliacaoRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AvaliacaoService {
    private AvaliacaoRepository repository;

    public AvaliacaoService() {
        this.repository = new AvaliacaoRepository();
    }

    public Avaliacao criar(String avaliadorId, String usuarioAvaliadoId, int nota, String comentario) throws IOException {
        if (usuarioAvaliadoId == null || usuarioAvaliadoId.isEmpty() || avaliadorId == null || avaliadorId.isEmpty()) {
            throw new IllegalArgumentException("Avaliador e usuário avaliado são obrigatórios");
        }

        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("Nota deve estar entre 1 e 5");
        }

        Avaliacao avaliacao = new Avaliacao(
                UUID.randomUUID().toString(),
                avaliadorId,
                usuarioAvaliadoId,
                nota,
                comentario,
                LocalDateTime.now()
        );
        repository.inserir(avaliacao);
        return avaliacao;
    }

    public List<Avaliacao> listar() {
        return repository.listar();
    }

    public Optional<Avaliacao> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<Avaliacao> buscarPorUsuarioAvaliado(String usuarioAvaliadoId) {
        return repository.buscarPorUsuarioAvaliado(usuarioAvaliadoId);
    }

    public List<Avaliacao> buscarPorAvaliador(String avaliadorId) {
        return repository.buscarPorAvaliador(avaliadorId);
    }

    public double calcularMediaAvaliacoes(String usuarioAvaliadoId) {
        return repository.calcularMediaAvaliacoes(usuarioAvaliadoId);
    }

    public Avaliacao atualizar(String id, String avaliadorId, String usuarioAvaliadoId, int nota, String comentario) throws IOException {
        Optional<Avaliacao> avaliacao = repository.buscarPorId(id);
        if (avaliacao.isEmpty()) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }

        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("Nota deve estar entre 1 e 5");
        }

        avaliacao.get().setAvaliadorId(avaliadorId);
        avaliacao.get().setUsuarioAvaliadoId(usuarioAvaliadoId);
        avaliacao.get().setNota(nota);
        avaliacao.get().setComentario(comentario);

        repository.atualizar(avaliacao.get());
        return avaliacao.get();
    }

    public void excluir(String id) throws IOException {
        Optional<Avaliacao> avaliacao = repository.buscarPorId(id);
        if (avaliacao.isEmpty()) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }
        repository.excluirPorId(id);
    }
}

