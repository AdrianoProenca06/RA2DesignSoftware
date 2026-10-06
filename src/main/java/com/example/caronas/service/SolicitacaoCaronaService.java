package com.example.caronas.service;

import com.example.caronas.model.SolicitacaoCarona;
import com.example.caronas.repository.SolicitacaoCaronaRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SolicitacaoCaronaService {
    private SolicitacaoCaronaRepository repository;

    public SolicitacaoCaronaService() {
        this.repository = new SolicitacaoCaronaRepository();
    }

    public SolicitacaoCarona criar(String passageiroId, String caronaId) throws IOException {
        if (passageiroId == null || passageiroId.isEmpty() || caronaId == null || caronaId.isEmpty()) {
            throw new IllegalArgumentException("Passageiro e carona são obrigatórios");
        }

        SolicitacaoCarona solicitacao = new SolicitacaoCarona(
                UUID.randomUUID().toString(),
                passageiroId,
                caronaId,
                "pendente",
                LocalDateTime.now()
        );
        repository.inserir(solicitacao);
        return solicitacao;
    }

    public List<SolicitacaoCarona> listar() {
        return repository.listar();
    }

    public Optional<SolicitacaoCarona> buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public List<SolicitacaoCarona> buscarPorPassageiro(String passageiroId) {
        return repository.buscarPorPassageiro(passageiroId);
    }

    public List<SolicitacaoCarona> buscarPorCarona(String caronaId) {
        return repository.buscarPorCarona(caronaId);
    }

    public List<SolicitacaoCarona> buscarPorStatus(String status) {
        return repository.buscarPorStatus(status);
    }

    public SolicitacaoCarona aceitar(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        solicitacao.get().setStatus("aceita");
        repository.atualizar(solicitacao.get());
        return solicitacao.get();
    }

    public SolicitacaoCarona atualizar(String id, String passageiroId, String caronaId, String status) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        solicitacao.get().setPassageiroId(passageiroId);
        solicitacao.get().setCaronaId(caronaId);
        solicitacao.get().setStatus(status);
        repository.atualizar(solicitacao.get());
        return solicitacao.get();
    }

    public SolicitacaoCarona recusar(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        solicitacao.get().setStatus("recusada");
        repository.atualizar(solicitacao.get());
        return solicitacao.get();
    }

    public void excluir(String id) throws IOException {
        Optional<SolicitacaoCarona> solicitacao = repository.buscarPorId(id);
        if (solicitacao.isEmpty()) {
            throw new IllegalArgumentException("Solicitação não encontrada");
        }
        repository.excluirPorId(id);
    }
}

